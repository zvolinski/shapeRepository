package com.example.exerciese;

import com.example.exerciese.exception.exception.ShapeInvalidPerimetersException;
import com.example.exerciese.exception.exception.ShapeInvalidTypeException;
import com.example.exerciese.exception.exception.ShapeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)

public class ShapeServiceTest {

    @Mock
    private ShapeValidator shapeValidator;

    @Mock
    private ShapeRepository shapeRepository;

    @InjectMocks
    private ShapeService shapeService;

    @BeforeEach
    void setUp() {
        Circle circle = new Circle();
        Map<String, Shape> shapeMap = Map.of("Circle", circle);
        shapeService = new ShapeService(shapeRepository, shapeMap, shapeValidator);
    }

    @Test
    void itShouldSaveShape() {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(List.of(5.0));

        Circle prototype1 = new Circle();
        prototype1.setPerimeters(List.of(5.0));

        when(shapeRepository.save(any())).thenReturn(prototype1);
        Shape shape = shapeService.saveShape(shapeRequest);

        verify(shapeValidator, times(1)).validateShapeRequest(shapeRequest);

        assertEquals(shapeRequest.getPerimeters().get(0), shape.getPerimeters().get(0));
        assertEquals(shapeRequest.getPerimeters().size(), shape.getPerimeters().size());
        assertEquals(shapeRequest.getType(), shape.getClass().getSimpleName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", """
            """, ",'", "InvalidType"})
    void itShouldNotSaveShapeWhenTypeIsInvalid() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("InvalidType"); // Invalid type
        shapeRequest.setPerimeters(List.of(5.00));

        doThrow(new ShapeInvalidTypeException("Invalid shape type"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        // When & Then
        assertThrows(ShapeInvalidTypeException.class, () -> shapeService.saveShape(shapeRequest));

        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldNotSaveShapeWhenTypeIsNull() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType(null);
        shapeRequest.setPerimeters(List.of(5.00));

        doThrow(ShapeInvalidTypeException.class)
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        // When & Then
        assertThrows(ShapeInvalidTypeException.class, () -> shapeService.saveShape(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldNotSaveShapeWhenPerimetersAreEmpty() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(Collections.emptyList()); // Empty list

        doThrow(new ShapeInvalidPerimetersException("Perimeters cannot be empty"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        // When & Then
        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeService.saveShape(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldNotSaveShapeWhenPerimetersAreNull() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(null); // Perimeters cannot be null

        doThrow(new ShapeInvalidPerimetersException("Perimeters cannot be null"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        // When & Then
        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeService.saveShape(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldNotSaveShapeWhenPerimetersContainNegative() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(List.of(5.00, -2.00)); // Invalid perimeter

        doThrow(new ShapeInvalidPerimetersException("All perimeters must be positive numbers"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        // When & Then
        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeService.saveShape(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldGetShapeByType() {
        //Given
        Shape circle = new Circle();
        circle.setPerimeters(List.of(5.00, 6.00));

        Shape rectangle = new Rectangle();
        rectangle.setPerimeters(List.of(7.00, 8.00));

        List<Shape> shapes = List.of(circle, rectangle);

        when(shapeRepository.findByType("Circle")).thenReturn(shapes);

        shapeService.getShapesByType("Circle");

        verify(shapeRepository, times(1)).findByType("Circle");

        assertThat(shapes).containsExactly(circle, rectangle);
        assertEquals(circle.getPerimeters(), shapes.get(0).getPerimeters());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", """
            """, ",'", "InvalidType"})
    void itShouldNotGetShape_BecauseTypeIsInvalid() {

        String invalidType = "InvalidType";//Invalid data to search in database

        // Return empty_list if type is not in database
        when(shapeRepository.findByType(invalidType)).thenReturn(Collections.emptyList());

        // Throws exception when list in database is empty
        assertThrows(ShapeNotFoundException.class, () -> shapeService.getShapesByType(invalidType));

        //Verify that method is invoked once
        verify(shapeRepository, times(1)).findByType(invalidType);
    }

    @Test
    void itShouldNotGetShape_WhenTypeIsNull() {
        // Given
        String type = null;

        when(shapeRepository.findByType(type)).thenReturn(Collections.emptyList());

        assertThrows(ShapeNotFoundException.class, () -> shapeService.getShapesByType(type));

        verify(shapeRepository, times(1)).findByType(type);
    }

    //To Do
    @Test
    void itShouldUpdateShape() {
        //Given
        Shape circle = new Circle();
        circle.setId(1L);
        List<Double> perimeter = new ArrayList<>();
        perimeter.add(5.00);
        circle.setPerimeters(perimeter);

        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> updatePerimeter = new ArrayList<>();
        updatePerimeter.add(7.00);
        shapeRequest.setPerimeters(updatePerimeter);
        shapeValidator.validateShapeRequest(shapeRequest);

        when(shapeRepository.findById(1L)).thenReturn(Optional.of(circle));

        //When
        Shape updatedShape = shapeService.updateShape(shapeRequest, circle.getId());

        //Then
        verify(shapeRepository, times(1)).save(any(Shape.class));
        assertEquals(updatePerimeter, circle.getPerimeters());
        assertEquals(shapeRequest.getPerimeters().get(0), updatedShape.getPerimeters().get(0));
        assertEquals(shapeRequest.getType(), updatedShape.getClass().getSimpleName());
    }
}
