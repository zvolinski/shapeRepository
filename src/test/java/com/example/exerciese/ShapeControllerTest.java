package com.example.exerciese;

import com.example.exerciese.exception.exception.ShapeInvalidTypeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = ExercieseApplication.class)
@AutoConfigureMockMvc

public class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShapeValidator shapeValidator;

    @Autowired
    private ShapeRepository shapeRepository;
    @Autowired
    private ShapeService shapeService;
    @Autowired
    private ShapeController shapeController;

    @Test
    void itShouldSaveShape() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        shapeRequest.setPerimeters(perimeters);


        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Shape savedShapeCircle = objectMapper.readValue(response, Circle.class);


        assertEquals(shapeRequest.getPerimeters(), savedShapeCircle.getPerimeters());
        assertEquals(shapeRequest.getType(), savedShapeCircle.getClass().getSimpleName());
    }

    @Test
    void itShouldGetShapeByType() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", shapeRequest.getType())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Circle"))
                .andExpect(jsonPath("$[0].perimeters[0]").value(5.0))
                .andExpect(jsonPath("$[0].perimeters[1]").value(6.0));
    }

    @Test
    void itShouldUpdateShape() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        shapeRequest.setPerimeters(List.of(5.00, 6.00));

        ShapeRequest updateShape = new ShapeRequest();
        updateShape.setPerimeters(List.of(7.00, 8.00));

        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long shapeId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", shapeRequest.getType())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Circle"))
                .andExpect(jsonPath("$[0].perimeters[0]").value(5.0))
                .andExpect(jsonPath("$[0].perimeters[1]").value(6.0));

        mockMvc.perform(put("/api/v1/shapes/{id}", shapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateShape)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", shapeRequest.getType())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Circle"))
                .andExpect(jsonPath("$[0].perimeters[0]").value(7.0))
                .andExpect(jsonPath("$[0].perimeters[1]").value(8.0));
    }
}


