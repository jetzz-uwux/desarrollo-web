package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
}