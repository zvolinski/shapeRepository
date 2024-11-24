package com.example.exerciese;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = ExercieseApplication.class)
@AutoConfigureMockMvc

public class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShapeRepository shapeRepository;
    @Autowired
    private ShapeService shapeService;
    @Autowired
    private ShapeController shapeController;

    @Test
    void itShouldSaveShapeCircle() throws Exception {

        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);


        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Shape circle = objectMapper.readValue(response, Circle.class);
        Shape savedShapeCircle = shapeRepository.save(circle);


        assertEquals(shapeRequest.getPerimeters(), savedShapeCircle.getPerimeters());
        assertEquals(shapeRequest.getType(), savedShapeCircle.getClass().getSimpleName());
    }


}


