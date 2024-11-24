package com.example.exerciese;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ShapeService {
    private final ShapeRepository shapeRepository;


    Shape saveCircle(ShapeRequest shapeRequest) {
        Shape circle = new Circle();
        circle.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(circle);
    }


}
