package com.example.exerciese;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShapeValidator {

    private final Map<String, Shape> shapeMap;

    public ShapeValidator(Map<String, Shape> shapeMap) {
        this.shapeMap = shapeMap;
    }

    public void validateShapeRequest(ShapeRequest shapeRequest) {
        validateType(shapeRequest.getType());
        validatePerimeters(shapeRequest.getPerimeters());
    }

    private void validateType(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
        if (!shapeMap.containsKey(type)) {
            throw new IllegalArgumentException("Invalid shape type " + type);
        }
    }

    private void validatePerimeters(List<Double> perimeters) {
        if (perimeters == null || perimeters.isEmpty()) {
            throw new IllegalArgumentException("Perimeters cannot be null or empty");
        }
        if (perimeters.stream().anyMatch(p -> p == null || p <= 0)) {
            throw new IllegalArgumentException("All perimeters must be positive numbers");
        }
    }

}