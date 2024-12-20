package com.example.exerciese;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<List<ShapeDTO>> getShapesByType(@RequestParam String type) {
        List<ShapeDTO> shapes = shapeService.getShapesByType(type);
        return ResponseEntity.ok(shapes);
    }

}
