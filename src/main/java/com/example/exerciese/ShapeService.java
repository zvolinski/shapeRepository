package com.example.exerciese;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShapeService {
    private final ShapeRepository shapeRepository;
    private final Map<String, Shape> shapeMap;

    Shape saveShape(ShapeRequest shapeRequest) {
        Shape prototype = shapeMap.get(shapeRequest.getType());
        Shape shape = prototype.clone();
        shape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(shape);
    }

    List<ShapeDTO> getShapesByType(String type) {
        List<Shape> shapes = shapeRepository.findByType(type);
        return shapes.stream().map(ShapeDTO::fromEntity).toList();
        Shape shape = shapeMap.get(shapeRequest.getType());
        shape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(shape);

    }

    Shape updateShape(ShapeRequest shapeRequest, Long id) {
        Shape updateShape = shapeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shape with" + id + "not found"));
        updateShape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(updateShape);
    }

}


