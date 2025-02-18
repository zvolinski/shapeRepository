package com.example.exerciese;

import com.example.exerciese.exception.exception.ShapeInvalidIdException;
import com.example.exerciese.exception.exception.ShapeNotFoundException;
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
                .orElseThrow(() -> new ShapeNotFoundException("No shapes found for the specified type"))
                .stream()
                .map(ShapeDTO::fromEntity)
                .toList();
    }

    Shape updateShape(ShapeRequest shapeRequest, Long id) {
        shapeValidator.validateId(id);
        Shape updateShape = shapeRepository.findById(id)
                .orElseThrow(() -> new ShapeInvalidIdException("Shape with ID" + id + "not found"));
        shapeValidator.validateShapeRequest(shapeRequest);
        updateShape.setPerimeters(shapeRequest.getPerimeters());
        return shapeRepository.save(updateShape);
    }
}

