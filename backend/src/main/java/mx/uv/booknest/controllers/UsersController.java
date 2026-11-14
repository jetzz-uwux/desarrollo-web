package mx.uv.booknest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.uv.booknest.models.Users;
import mx.uv.booknest.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UsersController {

    // Inyección de dependencias por campo que ya conoces
    @Autowired
    private UserService userService;

    // POST /api/users/register : Registrar una cuenta nueva de usuario
    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody Users user) {
        userService.registrarUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
    }


    // POST /api/users/login : Iniciar sesión en la plataforma
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users credentials) {
        boolean esValido = userService.validarLogin(credentials.getEmail(), credentials.getPasswordHash());
        
        if (esValido) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }
}