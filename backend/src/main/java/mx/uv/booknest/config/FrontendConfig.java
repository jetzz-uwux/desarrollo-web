package mx.uv.booknest.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sirve el frontend original desde la carpeta frontend/ del repositorio
 */
@Configuration
public class FrontendConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path frontend = encontrarFrontend();

        String ubicacion = frontend.toUri().toString();

        System.out.println("Frontend: " + ubicacion);

        registry.addResourceHandler("/**")
                .addResourceLocations(ubicacion);
    }

    private Path encontrarFrontend() {
        // Permite ejecutar Maven desde backend/ o desde la raiz del repositorio.
        Path directorioTrabajo = Paths.get("").toAbsolutePath().normalize();
        Path[] opciones = {
                directorioTrabajo.resolve("../frontend").normalize(),
                directorioTrabajo.resolve("frontend").normalize()
        };

        for (Path opcion : opciones) {
            if (Files.isRegularFile(opcion.resolve("index.html"))) {
                return opcion;
            }
        }

        throw new IllegalStateException(
                "No se encontro frontend/index.html. "
                + "Inicia Spring Boot desde desarrollo-web/backend "
                + "o desde desarrollo-web, manteniendo ambas carpetas juntas."
        );
    }
}
