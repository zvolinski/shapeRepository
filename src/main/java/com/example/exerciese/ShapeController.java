package com.example.exerciese;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public ResponseEntity<?> saveShape(@RequestBody @Valid ShapeRequest shapeRequest) {
        var Shape = shapeService.saveShape(shapeRequest);
        return ResponseEntity.ok().body(ShapeDTO.fromEntity(Shape));
    }

    @GetMapping
    public ResponseEntity<List<ShapeDTO>> getShapesByType(@RequestParam @NotBlank String type) {
        List<ShapeDTO> shapes = shapeService.getShapesByType(type);
        return ResponseEntity.ok(shapes);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateShape(@PathVariable Long id, @RequestBody @Valid ShapeRequest shapeRequest) {
        Shape updateShape = shapeService.updateShape(shapeRequest, id);
        return ResponseEntity.ok().body(ShapeDTO.fromEntity(updateShape));
    }

}
