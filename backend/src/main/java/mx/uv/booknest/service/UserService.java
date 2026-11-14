package mx.uv.booknest.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Users;
import mx.uv.booknest.repository.UsersRepository;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    // Registrar usuario nuevo
    public Users registrarUsuario(Users user) {
        // Validar campos obligatorios
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico es obligatorio");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }

        // Revisar si el correo o el usuario ya existen 
        List<Users> todosLosUsuarios = usersRepository.findAll();
        for (Users u : todosLosUsuarios) {
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
            }
            if (u.getUsername() != null && u.getUsername().equalsIgnoreCase(user.getUsername())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso");
            }
        }

        // Asignar fechas 
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        return usersRepository.save(user);
    }

    // Validar login buscando el correo 
    public boolean validarLogin(String email, String passwordPlana) {
        if (email == null || passwordPlana == null) {
            return false;
        }

        List<Users> todosLosUsuarios = usersRepository.findAll();
        for (Users u : todosLosUsuarios) {
            // Verificar si el correo coincide y la contraseña (passwordHash) es correcta
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)) {
                if (u.getPasswordHash() != null && u.getPasswordHash().equals(passwordPlana)) {
                    return true; // Login exitoso
                }
            }
        }

        return false; //Login fallido
    }
}