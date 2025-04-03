
package com.example.exerciese;

import java.util.List;

public record ShapeDTO(Long id, String type, List<Double> perimeters) {


    public static ShapeDTO fromEntity(Shape entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return new ShapeDTO(
                entity.getId(),
                entity.getClass().getSimpleName(),
                entity.getPerimeters() != null ? entity.getPerimeters() : List.of()
        );
    }
}