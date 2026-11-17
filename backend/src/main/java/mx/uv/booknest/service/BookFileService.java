package mx.uv.booknest.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.BookFile;
import mx.uv.booknest.models.Users;
import mx.uv.booknest.repository.BookFileRepository;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.UsersRepository;

/**
 * @author Hans Arturo
 */
@Service
public class BookFileService {

    private static final long MAX_FILE_SIZE = 50L * 1024 * 1024; // 50 MB
    private final Path storageLocation = Paths.get("uploads/pdfs").toAbsolutePath().normalize();

    private final BookFileRepository fileRepository;
    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;

    public BookFileService(BookFileRepository fileRepository, BookRepository bookRepository, UsersRepository usersRepository) {
        this.fileRepository = fileRepository;
        this.bookRepository = bookRepository;
        this.usersRepository = usersRepository;
        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar los archivos PDF", ex);
        }
    }

    public List<BookFile> listarConFiltros(Long bookId, String language, String isbn, String q) {
        if (language != null && !language.isBlank()) {
            validarIdioma(language);
        }

        if (isbn != null && !isbn.isBlank()) {
            String isbnNorm = normalizarYValidarIsbn(isbn);
            return fileRepository.findByEditionIsbn(isbnNorm).map(List::of).orElseGet(List::of);
        }

        if (bookId != null && language != null && !language.isBlank()) {
            if (!bookRepository.existsById(bookId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
            }
            return fileRepository.findByBookIdAndLanguage(bookId, language);
        }

        if (bookId != null) {
            if (!bookRepository.existsById(bookId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
            }
            return fileRepository.findByBookId(bookId);
        }

        if (language != null && !language.isBlank()) {
            return fileRepository.findByLanguage(language);
        }

        if (q != null && !q.isBlank()) {
            return fileRepository.findByOriginalFilenameContainingIgnoreCase(q.trim());
        }

        return fileRepository.findAll();
    }

    public List<BookFile> listarPorLibro(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
        }
        return fileRepository.findByBookId(bookId);
    }

    public BookFile obtenerPorId(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado"));
    }

    public BookFile subirArchivo(
            MultipartFile file,
            Long bookId,
            Long uploaderUserId,
            String language,
            String editionIsbn,
            Boolean isAuthorized) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un archivo PDF válido");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo excede el tamaño máximo permitido de 50 MB");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "documento.pdf");

        if (originalFilename.contains("..") || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe tener extensión .pdf y un nombre válido");
        }

        if (!esPdfReal(file)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El contenido del archivo no corresponde a un PDF real");
        }

        validarIdioma(language);

        if (isAuthorized == null || !isAuthorized) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se aceptan archivos autorizados, propios o de dominio público (RNF-12)");
        }

        if (bookId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del libro es obligatorio");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe"));

        Users uploader = null;
        if (uploaderUserId != null) {
            uploader = usersRepository.findById(uploaderUserId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario especificado no existe"));
        }

        String isbnNormalizado = null;
        if (editionIsbn != null && !editionIsbn.isBlank()) {
            isbnNormalizado = normalizarYValidarIsbn(editionIsbn);
            if (fileRepository.findByEditionIsbn(isbnNormalizado).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un archivo registrado con ese ISBN de edición");
            }
        }

        String checksum = calcularSha256(file);
        if (fileRepository.findByChecksumSha256(checksum).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este mismo archivo PDF ya fue subido previamente al catálogo");
        }

        String storedFilename = UUID.randomUUID().toString() + ".pdf";
        Path targetLocation = this.storageLocation.resolve(storedFilename).normalize();

        if (!targetLocation.startsWith(this.storageLocation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruta de almacenamiento inválida");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo PDF en el servidor");
        }

        BookFile bookFile = new BookFile();
        bookFile.setBook(book);
        bookFile.setUploaderUser(uploader);
        bookFile.setFilePath(storedFilename);
        bookFile.setFileSizeBytes(file.getSize());
        bookFile.setLanguage(language);
        bookFile.setEditionIsbn(isbnNormalizado);
        bookFile.setIsAuthorized(true);
        bookFile.setOriginalFilename(originalFilename);
        bookFile.setChecksumSha256(checksum);

        return fileRepository.save(bookFile);
    }

    public BookFile actualizarMetadatos(Long id, BookFile datosActualizados) {
        BookFile existente = obtenerPorId(id);

        if (datosActualizados == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de actualización son obligatorios");
        }

        if (datosActualizados.getLanguage() != null) {
            validarIdioma(datosActualizados.getLanguage());
            existente.setLanguage(datosActualizados.getLanguage());
        }

        if (datosActualizados.getEditionIsbn() != null) {
            if (datosActualizados.getEditionIsbn().isBlank()) {
                existente.setEditionIsbn(null);
            } else {
                String isbnNorm = normalizarYValidarIsbn(datosActualizados.getEditionIsbn());
                Optional<BookFile> duplicado = fileRepository.findByEditionIsbn(isbnNorm);
                if (duplicado.isPresent() && !duplicado.get().getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro archivo registrado con ese ISBN de edición");
                }
                existente.setEditionIsbn(isbnNorm);
            }
        }

        if (datosActualizados.getOriginalFilename() != null) {
            String nombreLimpio = StringUtils.cleanPath(datosActualizados.getOriginalFilename().trim());
            if (nombreLimpio.isBlank() || nombreLimpio.contains("..") || !nombreLimpio.toLowerCase().endsWith(".pdf")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de archivo debe ser válido y terminar en .pdf");
            }
            existente.setOriginalFilename(nombreLimpio);
        }

        if (datosActualizados.getIsAuthorized() != null) {
            existente.setIsAuthorized(datosActualizados.getIsAuthorized());
        }

        return fileRepository.save(existente);
    }

    public void eliminar(Long id) {
        BookFile existente = obtenerPorId(id);
        Path filePath = this.storageLocation.resolve(existente.getFilePath()).normalize();
        fileRepository.delete(existente);
        try {
            if (filePath.startsWith(this.storageLocation)) {
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ex) {
            // El registro en BD ya fue eliminado; se ignora si el archivo físico no estaba en disco
        }
    }

    public Resource cargarArchivoParaDescarga(BookFile bookFile) {
        if (bookFile.getIsAuthorized() == null || !bookFile.getIsAuthorized()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este archivo no está autorizado para su descarga");
        }
        try {
            Path filePath = this.storageLocation.resolve(bookFile.getFilePath()).normalize();
            if (!filePath.startsWith(this.storageLocation)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruta de archivo inválida");
            }
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El archivo físico no se encontró en el servidor");
            }
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo leer el archivo para su descarga");
        }
    }

    private void validarIdioma(String language) {
        if (language == null || (!language.equals("es") && !language.equals("en"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El idioma debe ser 'es' (español) o 'en' (inglés)");
        }
    }

    private String normalizarYValidarIsbn(String editionIsbn) {
        String isbnNormalizado = editionIsbn.replaceAll("[\\s-]", "");
        if (!isbnNormalizado.matches("^[0-9Xx]{10,17}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato del ISBN no es válido");
        }
        return isbnNormalizado;
    }

    private boolean esPdfReal(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[5];
            int read = is.read(header);
            if (read < 5) {
                return false;
            }
            return header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46 && header[4] == 0x2D;
        } catch (IOException e) {
            return false;
        }
    }

    private String calcularSha256(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al calcular el checksum del archivo");
        }
    }
}