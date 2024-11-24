package com.example.exerciese;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)

public class ShapeServiceTest {

    @Mock
    private ShapeRepository shapeRepository;
    @InjectMocks
    private ShapeService shapeService;

    @Test
    void itShouldSave() {
        //Given
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeter = new ArrayList<>();
        perimeter.add(5.00);
        shapeRequest.setPerimeters(perimeter);


        //When
        shapeService.saveCircle(shapeRequest);

        //Then
        verify(shapeRepository, times(1)).save(any(Shape.class));
    }
}
