package com.example.exerciese;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequiredArgsConstructor


public class ShapeController {
    private final ShapeService shapeService;

//    @PostMapping("/api/v1/shapes")
//    public ResponseEntity<?> saveCircle(@Valid @RequestBody ShapeRequest shapeRequest) {
//        var save = shapeService.saveCircle(shapeRequest);
//        return ResponseEntity.ok(save);
//    }

    @PostMapping("/api/v1/shapes")
    public ResponseEntity<?> save(@RequestBody @Valid ShapeRequest shapeRequest) {
        var Shape = shapeService.saveCircle(shapeRequest);
        return ResponseEntity.ok().body(ShapeDTO.fromEntity(Shape));
    }
}
