package mx.uv.booknest.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.CollectionBooks;
import mx.uv.booknest.models.UserCollection;
import mx.uv.booknest.models.Users;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.CollectionBooksRepository;
import mx.uv.booknest.repository.UserCollectionRepository;
import mx.uv.booknest.repository.UsersRepository;

@Service
public class UserCollectionService {

    @Autowired
    private UserCollectionRepository collectionRepository;

    @Autowired
    private CollectionBooksRepository collectionBooksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookRepository bookRepository;

    //listar las colecciones
    public List<UserCollection> listarTodas() {
        return collectionRepository.findAll();
    }

    //Obtener colección por ID
    public UserCollection obtenerPorId(Long id) {
        Optional<UserCollection> resultado = collectionRepository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada");
        }
        return resultado.get();
    }

    // Crear nueva colección
    public UserCollection crearColeccion(UserCollection collection, Long userId) {
        if (collection.getName() == null || collection.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la colección es obligatorio");
        }

        Users user = usersRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        collection.setUser(user);

        if (collection.getCreatedAt() == null) {
            collection.setCreatedAt(OffsetDateTime.now());
        }
        collection.setUpdatedAt(OffsetDateTime.now());

        return collectionRepository.save(collection);
    }

    //Añadir un libro a una colección
    public CollectionBooks agregarLibroAColeccion(Long collectionId, Long bookId) {
        UserCollection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada"));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        // Crear la relación
        CollectionBooks collectionBook = new CollectionBooks();
        collectionBook.setUserCollection(collection);
        collectionBook.setBook(book);

        return collectionBooksRepository.save(collectionBook);
    }
}