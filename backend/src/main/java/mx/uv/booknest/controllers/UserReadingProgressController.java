package mx.uv.booknest.controllers;

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

    // FUNCION: POST /api/progress : Actualizar el estado de lectura y página actual
    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<String> actualizarProgreso(
            @RequestBody UserReadingProgress progress,
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        
        progressService.guardarOActualizar(progress, userId, bookId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Progreso de lectura actualizado exitosamente");
    }
}