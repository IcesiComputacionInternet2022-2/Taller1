package com.icesi.edu.zoo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesi.edu.zoo.constant.AnimalErrorCode;
import com.icesi.edu.zoo.constant.CondorCharacteristics;
import com.icesi.edu.zoo.dto.AnimalDTO;
import com.icesi.edu.zoo.error.exception.AnimalError;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimalIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private final String RESOURCE_PATH = "createAnimal.json";
    private final String PARENT_RESOURCE_PATH = "createParent.json";
    private final String POST_PATH = "/animal/";

    @BeforeEach
    private void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    //For post request that are expected to be 200 OK
    @SneakyThrows
    private AnimalDTO performPostCorrectRequest(String body, String path) {
        body = parseResourceToString(body);
        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(res.getResponse().getContentAsString(), AnimalDTO.class);
    }

    //For post request that are expected to throw an exception
    @SneakyThrows
    private AnimalError performPostErrorRequest(String body, String path, ResultMatcher expectedResult) {
        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(expectedResult)
                .andReturn();
        return objectMapper.readValue(res.getResponse().getContentAsString(), AnimalError.class);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalSuccessfully() {
        AnimalDTO sent = getAnimalTemplate();
        AnimalDTO dto = performPostCorrectRequest(RESOURCE_PATH, POST_PATH);
        assertThat(dto, hasProperty("id", is(notNullValue())));
        assertThat(dto, hasProperty("name", is(sent.getName())));
        assertThat(dto, hasProperty("weight", is(sent.getWeight())));
        assertThat(dto, hasProperty("height", is(sent.getHeight())));
        assertThat(dto, hasProperty("sex", is(sent.getSex())));
        assertThat(dto, hasProperty("age", is(sent.getAge())));
        assertThat(dto, hasProperty("arrivalDate", is(sent.getArrivalDate())));
    }

    private void verifyErrorAttributes(AnimalError error, AnimalErrorCode animalErrorCode) {
        assertThat(error, hasProperty("code", is(animalErrorCode)));
        assertThat(error, hasProperty("message", is(animalErrorCode.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithNameLongerThanAllowed() {
        AnimalDTO template = getAnimalTemplate();
        template.setName(StringUtils.repeat('a', 121));
        String body = objectMapper.writeValueAsString(template);
        AnimalError animalError = performPostErrorRequest(body, POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_02);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithNumbersInName() {
        AnimalDTO template = getAnimalTemplate();
        template.setName("numeros123");
        String body = objectMapper.writeValueAsString(template);
        AnimalError animalError = performPostErrorRequest(body, POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_02);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithInvalidSex() {
        AnimalDTO template = getAnimalTemplate();
        template.setName("Otro Nombre");
        template.setSex('J');
        String body = objectMapper.writeValueAsString(template);
        AnimalError animalError = performPostErrorRequest(body, POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_03);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithInvalidDate() {
        AnimalDTO template = getAnimalTemplate();
        template.setName("Otro Nombre");
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.YEAR, 5);
        template.setArrivalDate(futureDate.getTime());
        String body = objectMapper.writeValueAsString(template);
        AnimalError animalError = performPostErrorRequest(body, POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_04);
    }

    @Test
    @AfterTestMethod(value = "testCreateAnimalSuccessfully")
    @SneakyThrows
    public void testCreateRepeatedAnimal() {
        AnimalError animalError = performPostErrorRequest(objectMapper.writeValueAsString(getAnimalTemplate()), POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_O5);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithNoRegisteredParent() {
        AnimalDTO template = getAnimalTemplate();
        template.setName("Otro Nombre");
        template.setMaleParentName("No existo");
        AnimalError animalError = performPostErrorRequest(objectMapper.writeValueAsString(template), POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_06);
    }

    @SneakyThrows
    private AnimalDTO registerParent() {
        performPostCorrectRequest(PARENT_RESOURCE_PATH, POST_PATH);
        return objectMapper.readValue(parseResourceToString(PARENT_RESOURCE_PATH), AnimalDTO.class);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithParentNotMatchingSex() {
        AnimalDTO parent = registerParent();
        AnimalDTO child = getAnimalTemplate();
        child.setName("otro nombre");
        child.setFemaleParentName(parent.getName());
        AnimalError animalError = performPostErrorRequest(objectMapper.writeValueAsString(child), POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_08);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithInvalidWeight() {
        AnimalDTO template = getAnimalTemplate();
        template.setName("Otro Nombre");
        template.setWeight(CondorCharacteristics.WEIGHT.getMax() + 1);
        AnimalError animalError = performPostErrorRequest(objectMapper.writeValueAsString(template), POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_10);
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalWithInvalidHeight() {
        AnimalDTO template = getAnimalTemplate();
        template.setName("Otro Nombre");
        template.setWeight(CondorCharacteristics.HEIGHT.getMax() + 1);
        AnimalError animalError = performPostErrorRequest(objectMapper.writeValueAsString(template), POST_PATH, status().isBadRequest());
        verifyErrorAttributes(animalError, AnimalErrorCode.CODE_10);
    }

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resources = new ClassPathResource(classPath);
        try(Reader reader = new InputStreamReader(resources.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    @SneakyThrows
    private AnimalDTO getAnimalTemplate() {
        String body = parseResourceToString(RESOURCE_PATH);
        return objectMapper.readValue(body, AnimalDTO.class);
    }

}