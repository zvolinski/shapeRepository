package com.example.exerciese;

import com.example.exerciese.exception.exception.ShapeInvalidIdException;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        prototype1.setPerimeters(shapeRequest.getPerimeters());

        when(shapeRepository.save(any())).thenReturn(prototype1);
        Shape shape = shapeService.saveShape(shapeRequest);


        assertEquals(shapeRequest.getPerimeters().get(0), shape.getPerimeters().get(0));
        assertEquals(shapeRequest.getPerimeters().size(), shape.getPerimeters().size());
        assertEquals(shapeRequest.getType(), shape.getClass().getSimpleName());
        assertEquals(prototype1.getRequiredParametersCount(), shape.getRequiredParametersCount());
    }

    @Test
    void itShouldThrowExceptionWhenPerimetersCountIsInvalid() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(List.of(5.0, 6.0)); // Błędna liczba parametrów

        doThrow(new ShapeInvalidPerimetersException("Invalid perimeters count"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));

        verify(shapeValidator, times(1)).validateShapeRequest(shapeRequest);
        verify(shapeRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", """
            """, ",'", "InvalidType"})
    void itShouldNotSaveShapeWhenTypeIsInvalid() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("InvalidType"); // Invalid type
        shapeRequest.setPerimeters(List.of(5.0));

        doThrow(new ShapeInvalidTypeException("Invalid shape type"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        assertThrows(ShapeInvalidTypeException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));

        // When & Then
        verify(shapeValidator, times(1)).validateShapeRequest(shapeRequest);
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
        assertThrows(ShapeInvalidTypeException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));
        verify(shapeValidator, times(1)).validateShapeRequest(shapeRequest);
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
        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));
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
        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldNotSaveShapeWhenPerimetersContainNegative() {
        // Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(List.of(-2.00)); // Invalid perimeter

        doThrow(new ShapeInvalidPerimetersException("All perimeters must be positive numbers"))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        // When & Then
        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldNotSaveShapeWhenInvalidPerimetersContain() {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(List.of(5.0, 6.0, 7.0));

        doThrow(new ShapeInvalidPerimetersException("Incorrect quantity of perimeters for: " + shapeRequest.getType()))
                .when(shapeValidator).validateShapeRequest(shapeRequest);

        assertThrows(ShapeInvalidPerimetersException.class, () -> shapeValidator.validateShapeRequest(shapeRequest));
        verify(shapeRepository, never()).save(any());
    }

    @Test
    void itShouldGetShapeByType() {
        //Given
        Shape circle = new Circle();
        circle.setPerimeters(List.of(5.0));

        Shape rectangle = new Rectangle();
        rectangle.setPerimeters(List.of(7.0, 8.0));

        List<Shape> shapes = List.of(circle, rectangle);

        when(shapeRepository.findByType("Circle")).thenReturn(shapes);

        shapeService.getShapesByType("Circle");

        verify(shapeRepository, times(1)).findByType("Circle");

        assertThat(shapes).containsExactly(circle, rectangle);
        assertEquals(rectangle.getPerimeters().size(), rectangle.getRequiredParametersCount());
        assertEquals(circle.getPerimeters().size(), circle.getRequiredParametersCount());
        assertEquals(circle.getPerimeters(), shapes.get(0).getPerimeters());
        assertEquals(rectangle.getPerimeters(), shapes.get(1).getPerimeters());
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
    void itShouldThrowException_WhenTypeIsNull() {
        // Given
        String type = null;

        when(shapeRepository.findByType(type)).thenReturn(Collections.emptyList());

        assertThrows(ShapeNotFoundException.class, () -> shapeService.getShapesByType(type));

        verify(shapeRepository, times(1)).findByType(type);
    }

    @Test
    void itShouldThrowException_WhenShapeNotFoundById() {
        // Given
        Long id = 99L;
        ShapeRequest shapeRequest = new ShapeRequest();

        when(shapeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ShapeInvalidIdException.class, () -> shapeService.updateShape(shapeRequest, id));

        verify(shapeRepository, times(1)).findById(id);
    }

    @Test
    void itShouldFindShapeById() {
        Circle existingCircle = new Circle();
        existingCircle.setId(1L);
        existingCircle.setPerimeters(List.of(5.0));

        when(shapeRepository.findById(existingCircle.getId())).thenReturn(Optional.of(existingCircle));

        Optional<Shape> result = shapeRepository.findById(existingCircle.getId());

        assertTrue(result.isPresent());
        assertEquals(existingCircle, result.get());

        verify(shapeRepository, times(1)).findById(existingCircle.getId());
    }

    @Test
    void itShouldUpdateShape() {
        //Given
        Circle previousCircle = new Circle();
        previousCircle.setId(1L);
        previousCircle.setPerimeters(List.of(6.0));

        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setPerimeters(List.of(5.0));

        Circle updatedCircle = new Circle();
        updatedCircle.setId(previousCircle.getId());
        updatedCircle.setPerimeters(shapeRequest.getPerimeters());

        //When & then
        when(shapeRepository.findById(previousCircle.getId())).thenReturn(Optional.of(previousCircle));
        when(shapeRepository.save(any())).thenReturn(updatedCircle);

        Shape result = shapeService.updateShape(shapeRequest, previousCircle.getId());

        // Then
        verify(shapeRepository, times(1)).save(any());
        assertEquals(updatedCircle.getId(), result.getId());
        assertEquals(updatedCircle.getPerimeters(), result.getPerimeters());
    }

    @Test
    void itShouldNotChangeShapeType_WhenUpdatingShape() {
        // Given
        Circle existingShape = new Circle();
        existingShape.setId(1L);

        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setPerimeters(List.of(7.0));

        when(shapeRepository.findById(existingShape.getId())).thenReturn(Optional.of(existingShape));

        shapeService.updateShape(shapeRequest, existingShape.getId());

        verify(shapeRepository, times(1)).findById(existingShape.getId());
        verify(shapeRepository, times(1)).save(existingShape);

        assertThat(existingShape).isInstanceOf(Circle.class);
    }
}
