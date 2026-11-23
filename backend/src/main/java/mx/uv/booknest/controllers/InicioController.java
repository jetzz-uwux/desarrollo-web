package mx.uv.booknest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Abre el frontend original desde el puerto configurado en Spring Boot. */
@Controller
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/index.html";
    }
}
