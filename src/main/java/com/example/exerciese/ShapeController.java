package com.example.exerciese;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shapes")
@RequiredArgsConstructor
public class ShapeController {
    private final ShapeService shapeService;


    @PostMapping
    public ResponseEntity<?> saveShape(@RequestBody ShapeRequest shapeRequest) {
        Shape shape = shapeService.saveShape(shapeRequest);
        return ResponseEntity.ok().body(ShapeDTO.fromEntity(shape));
    }

    @GetMapping
    public ResponseEntity<List<ShapeDTO>> getShapesByType(@RequestParam String type) {
        List<ShapeDTO> shapes = shapeService.getShapesByType(type);
        return ResponseEntity.ok(shapes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShape(@PathVariable Long id, @RequestBody ShapeRequest shapeRequest) {
        Shape updateShape = shapeService.updateShape(shapeRequest, id);
        return ResponseEntity.ok().body(ShapeDTO.fromEntity(updateShape));
    }

}
