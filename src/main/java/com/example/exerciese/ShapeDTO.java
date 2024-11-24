
package com.example.exerciese;

import java.util.List;

public record ShapeDTO(Long id, String type, List<Double> perimeters) {

    public static ShapeDTO fromEntity(Shape entity) {
        return new ShapeDTO(
                entity.getId(),
                entity.getClass().getSimpleName(),
                entity.getPerimeters()
        );

    }
}