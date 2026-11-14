package mx.uv.booknest.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.UserReadingProgress;
import mx.uv.booknest.models.Users;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.UserReadingProgressRepository;
import mx.uv.booknest.repository.UsersRepository;

@Service
public class UserReadingProgressService {

    @Autowired
    private UserReadingProgressRepository progressRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<UserReadingProgress> listarTodos() {
        return progressRepository.findAll();
    }

    public UserReadingProgress obtenerPorId(Long id) {
        Optional<UserReadingProgress> resultado = progressRepository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Progreso no encontrado");
        }
        return resultado.get();
    }

    public UserReadingProgress guardarOActualizar(UserReadingProgress progress, Long userId, Long bookId) {
        //Validar estado de lectura
        if (progress.getStatus() == null || 
            (!progress.getStatus().equals("pendiente") && 
             !progress.getStatus().equals("leyendo") && 
             !progress.getStatus().equals("terminado"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado debe ser: pendiente, leyendo o terminado");
        }

        //Validar páginas
        if (progress.getCurrent_page() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La página actual no puede ser menor a 0");
        }

        //Validar usuario 
        Users user = usersRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        //Validar libro
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        // Asignar entidades usuario y libro
        progress.setUser(user);
        progress.setBook(book);

        //Asignar fechas
        if (progress.getStatus().equals("terminado")) {
            if (progress.getFinishedAt() == null) {
                progress.setFinishedAt(OffsetDateTime.now());
            }
        } else {
            progress.setFinishedAt(null);
        }

        if (progress.getCreatedAt() == null) {
            progress.setCreatedAt(OffsetDateTime.now());
        }
        progress.setUpdatedAt(OffsetDateTime.now());

        return progressRepository.save(progress);
    }
}