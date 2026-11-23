package mx.uv.booknest.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.uv.booknest.models.UserReadingProgress;
import mx.uv.booknest.service.UserReadingProgressService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/progress")
public class UserReadingProgressController {

    @Autowired
    private UserReadingProgressService progressService;

    // FUNCION: GET /api/progress/user/{userId} : Obtener el progreso de lectura de un usuario para la tabla
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReadingProgress>> listarProgresoPorUsuario(@PathVariable Long userId) {
        List<UserReadingProgress> progresos = progressService.listarPorUsuario(userId);
        return ResponseEntity.ok(progresos);
    }

    // FUNCION: POST /api/progress/user/{userId}/book/{bookId} : Actualizar el estado de lectura y página actual
    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<String> actualizarProgreso(
            @RequestBody UserReadingProgress progress,
            @PathVariable Long userId,
            @PathVariable Long bookId) {

        progressService.guardarOActualizar(progress, userId, bookId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Progreso de lectura actualizado exitosamente");
    }
}