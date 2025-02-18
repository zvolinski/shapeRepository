package com.example.exerciese;

import com.example.exerciese.exception.exception.ShapeInvalidIdException;
import com.example.exerciese.exception.exception.ShapeInvalidPerimetersException;
import com.example.exerciese.exception.exception.ShapeInvalidTypeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ShapeValidator {

    private final Map<String, Shape> shapeMap;
    private final ShapeService shapeService;

    public ShapeValidator(Map<String, Shape> shapeMap, ShapeService shapeService) {
        this.shapeMap = shapeMap;
        this.shapeService = shapeService;
    }

    public void validateShapeRequest(ShapeRequest shapeRequest) {
        validateType(shapeRequest.getType());
        validatePerimeters(shapeRequest.getPerimeters());
    }


//
//    //Dla typu circle tabla ma posiadac nie wiecej, nie mniej jak jeden parametr// Dla innych tak samo(private valid) i publiczny konstruktor
//    private void validateAmountOfPerimetersForCircle(Circle circle, List<Double>perimeters) {
//        Optional.ofNullable(perimeters).stream().filter(pM -> pM.size() == 1)
//                .filter(pM -> !perimeters.isEmpty())
//    }


    private void validateType(String type) {
        Optional.ofNullable(type)
                .filter(t -> !t.isEmpty())
                .filter(t -> shapeMap.containsKey(t))
                .orElseThrow(() -> new ShapeInvalidTypeException("Type cannot be null or blank, or must be one of " + shapeMap.keySet()));
    }

    private void validatePerimeters(List<Double> perimeters) {
        Optional.ofNullable(perimeters)
                .filter(pM -> !pM.isEmpty()) // Sprawdza, czy lista nie jest pusta
                .filter(pM -> pM.stream().allMatch(p -> p != null && p > 0)) // Sprawdza poprawność wartości
                .orElseThrow(() -> new ShapeInvalidPerimetersException("Perimeters cannot be null, empty, or contain non-positive numbers"));

    }

    public void validateId(Long id) {
        Optional.ofNullable(id)
                .filter(i -> shapeMap.containsKey(i))
                .filter(i -> i > 0 && shapeMap.get(i) != null)
                .orElseThrow(() -> new ShapeInvalidIdException("Id cannot be null ,empty or must be one of " + shapeMap.keySet()));

    }

}