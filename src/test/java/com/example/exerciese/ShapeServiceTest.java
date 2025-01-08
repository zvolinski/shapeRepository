package com.example.exerciese;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)

public class ShapeServiceTest {

    @Mock
    private ShapeRepository shapeRepository;
    @InjectMocks
    private ShapeService shapeService;

    @Test
    void itShouldSaveShape() {
        //Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeter = new ArrayList<>();
        perimeter.add(5.00);
        shapeRequest.setPerimeters(perimeter);

        //When
        shapeService.saveShape(shapeRequest);

        //Then
        verify(shapeRepository, times(1)).save(any(Shape.class));
    }


    @Test
    void itShouldGetShapeByType() {

        //Given
        Shape circle = new Circle();
        List<Double> perimeter = new ArrayList<>();
        perimeter.add(5.00);
        circle.setPerimeters(perimeter);

        Shape rectangle = new Rectangle();
        List<Double> perimeter2 = new ArrayList<>();
        perimeter2.add(5.00);
        rectangle.setPerimeters(perimeter2);

        List<Shape> shapes = new ArrayList<>();
        shapes.add(circle);
        shapes.add(rectangle);

        //When
        shapeService.getShapesByType("type").forEach(shape -> {
        });

        //Then
        verify(shapeRepository, times(1)).findByType("type");
    }

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
        circle.setPerimeters(updatePerimeter);

        when(shapeRepository.findById(1L)).thenReturn(Optional.of(circle));

        //When
        shapeService.updateShape(shapeRequest, circle.getId());

        //Then
        verify(shapeRepository, times(1)).save(any(Shape.class));

    }
}
