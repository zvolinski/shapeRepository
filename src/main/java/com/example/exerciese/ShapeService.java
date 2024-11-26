package com.example.exerciese;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShapeService {
    private final ShapeRepository shapeRepository;
    private final Map<String, Shape> shapeMap;

    Shape saveShape(ShapeRequest shapeRequest) {
        Shape shape = shapeMap.get(shapeRequest.getType());
        shape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(shape);
    }


}


