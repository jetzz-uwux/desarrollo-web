package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mx.uv.booknest.models.BookFile;
import mx.uv.booknest.service.BookFileService;

/**
 * @author Hans Arturo
 */
@RestController
@RequestMapping("/api/files")
public class BookFileController {

    private final BookFileService service;

    public BookFileController(BookFileService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookFile> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public BookFile obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/book/{bookId}")
    public List<BookFile> obtenerPorLibro(@PathVariable Long bookId) {
        return service.listarPorLibro(bookId);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookFile subirArchivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bookId") Long bookId,
            @RequestParam(value = "uploaderUserId", required = false) Long uploaderUserId,
            @RequestParam("language") String language,
            @RequestParam(value = "editionIsbn", required = false) String editionIsbn,
            @RequestParam(value = "isAuthorized", defaultValue = "true") Boolean isAuthorized) {
        return service.subirArchivo(file, bookId, uploaderUserId, language, editionIsbn, isAuthorized);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        BookFile bookFile = service.obtenerPorId(id);
        Resource resource = service.cargarArchivoParaDescarga(bookFile);

        String filename = bookFile.getOriginalFilename() != null ? bookFile.getOriginalFilename() : "libro.pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}