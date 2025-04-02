package com.example.exerciese;

import com.example.exerciese.exception.exception.ShapeInvalidTypeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@Transactional


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
    void itShouldSaveShape() throws Exception {
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

        Shape savedShapeCircle = objectMapper.readValue(response, Circle.class);

        assertEquals(shapeRequest.getPerimeters(), savedShapeCircle.getPerimeters());
        assertEquals(shapeRequest.getType(), savedShapeCircle.getClass().getSimpleName());
    }

    @Test
    void itShouldThrowExceptionWhenPerimetersContainsNegative() throws Exception{
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(-2.0);
        perimeters.add(5.0);
        shapeRequest.setPerimeters(perimeters);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldThrowExceptionWhenInvalidShapeType() throws Exception{
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Invalid");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(2.0);
        perimeters.add(5.0);
        shapeRequest.setPerimeters(perimeters);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldThrowExceptionWhenTypeIsNull() throws Exception{
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType(null);
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(2.0);
        perimeters.add(5.0);
        shapeRequest.setPerimeters(perimeters);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldThrowExceptionWhenInvalidPerimeterCount() throws Exception{
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        shapeRequest.setPerimeters(perimeters);

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isBadRequest());
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
    void itShouldNotGetShapeBecauseTypeIsInvalid() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        String type = "InvalidShapeType";

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldNotGetShapeBecauseTypeIsNull() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        String type = null;

        mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/shapes")
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldUpdateShape() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        ShapeRequest updateShape = new ShapeRequest();
        updateShape.setType(shapeRequest.getType());
        List<Double> updatePerimeters = new ArrayList<>();
        updatePerimeters.add(7.0);
        updatePerimeters.add(8.0);
        updateShape.setPerimeters(updatePerimeters);

        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

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
                        .param("type", updateShape.getType())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Circle"))
                .andExpect(jsonPath("$[0].perimeters[0]").value(7.0))
                .andExpect(jsonPath("$[0].perimeters[1]").value(8.0));
    }

    @Test
    void itShouldNotUpdateShapeWhenPerimetersContainsNegative() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        ShapeRequest updateShape = new ShapeRequest();
        updateShape.setType(shapeRequest.getType());
        List<Double> updatePerimeters = new ArrayList<>();
        updatePerimeters.add(-7.0);
        updatePerimeters.add(8.0);
        updateShape.setPerimeters(updatePerimeters);

        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

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
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldNotUpdateShapeWhenInvalidPerimetersCount() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        ShapeRequest updateShape = new ShapeRequest();
        updateShape.setType(shapeRequest.getType());
        List<Double> updatePerimeters = new ArrayList<>();
        updatePerimeters.add(7.0);
        updatePerimeters.add(5.0);
        updatePerimeters.add(8.0);
        updateShape.setPerimeters(updatePerimeters);

        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

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
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldNotUpdateShapeWhenTypeIsInvalid() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        ShapeRequest updateShape = new ShapeRequest();
        updateShape.setType("InvalidShapeType");
        List<Double> updatePerimeters = new ArrayList<>();
        updatePerimeters.add(7.0);
        updatePerimeters.add(8.0);
        updateShape.setPerimeters(updatePerimeters);

        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

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
                .andExpect(status().isBadRequest());
    }

    @Test
    void itShouldNotUpdateShapeWhenTypeIsNull() throws Exception {
        ShapeRequest shapeRequest = new ShapeRequest();
        shapeRequest.setType("Circle");
        List<Double> perimeters = new ArrayList<>();
        perimeters.add(5.0);
        perimeters.add(6.0);
        shapeRequest.setPerimeters(perimeters);

        ShapeRequest updateShape = new ShapeRequest();
        updateShape.setType(null);
        List<Double> updatePerimeters = new ArrayList<>();
        updatePerimeters.add(7.0);
        updatePerimeters.add(8.0);
        updateShape.setPerimeters(updatePerimeters);

        String response = mockMvc.perform(post("/api/v1/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shapeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

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
                .andExpect(status().isBadRequest());
    }
}


