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

    // Autowired para inyectar las dependencias
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
    public ResponseEntity<?> login(@RequestBody Users credentials) {
        boolean esValido = userService.validarLogin(credentials.getEmail(), credentials.getPasswordHash());

        if (esValido) {
            Users usuario = userService.buscarPorEmail(credentials.getEmail());
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody Users user) {
        userService.actualizarUsuario(id, user);
        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

}