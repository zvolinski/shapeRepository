package com.example.exerciese;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShapeService {
    private final ShapeRepository shapeRepository;
    private final Map<String, Shape> shapeMap;
    private final ShapeValidator shapeValidator;


    Shape saveShape(ShapeRequest shapeRequest) {
        shapeValidator.validateShapeRequest(shapeRequest); //Input data validation
        Shape prototype = shapeMap.get(shapeRequest.getType());
        Shape shape = prototype.clone();
        shape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(shape);
    }

    List<ShapeDTO> getShapesByType(String type) {

        return Optional.of(shapeRepository.findByType(type))
                .filter(shapes -> !shapes.isEmpty())
                .orElseThrow(() -> new EntityNotFoundException("No shapes found for the specified type"))
                .stream()
                .map(ShapeDTO::fromEntity)
                .toList();
    }

    Shape updateShape(ShapeRequest shapeRequest, Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid ID" + id);
        }
        Shape updateShape = shapeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shape with ID" + id + "not found"));
        shapeValidator.validateShapeRequest(shapeRequest);
        updateShape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(updateShape);
    }
}

