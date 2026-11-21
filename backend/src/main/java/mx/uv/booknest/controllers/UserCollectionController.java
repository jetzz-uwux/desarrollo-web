package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.uv.booknest.models.UserCollection;
import mx.uv.booknest.service.UserCollectionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/collections")
public class UserCollectionController {

    @Autowired
    private UserCollectionService collectionService;

    // FUNCION: GET /api/collections : Listar todas las colecciones personalizadas
    @GetMapping
    public ResponseEntity<List<UserCollection>> listarTodas() {
        List<UserCollection> collections = collectionService.listarTodas();
        return ResponseEntity.ok(collections);
    }

    
    @PostMapping
    public ResponseEntity<String> crearColeccion(@RequestBody UserCollection collection) {
        collectionService.guardarColeccion(collection);
        return ResponseEntity.status(HttpStatus.CREATED).body("Colección creada exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarColeccion(@PathVariable Long id, @RequestBody UserCollection collection) {
        collectionService.actualizarColeccion(id, collection);
        return ResponseEntity.ok("Colección actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarColeccion(@PathVariable Long id) {
        collectionService.eliminarColeccion(id);
        return ResponseEntity.ok("Colección eliminada exitosamente");
    }

    @PostMapping("/{collectionId}/books/{bookId}")
    public ResponseEntity<String> agregarLibro(@PathVariable Long collectionId, @PathVariable Long bookId) {
        collectionService.agregarLibroAColeccion(collectionId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Libro agregado a la colección");
    }

}