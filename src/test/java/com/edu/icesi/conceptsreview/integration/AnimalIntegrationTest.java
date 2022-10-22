package com.edu.icesi.conceptsreview.integration;

import com.edu.icesi.conceptsreview.dto.AnimalDTO;
import com.edu.icesi.conceptsreview.dto.AnimalParentsDTO;
import com.edu.icesi.conceptsreview.dto.AnimalParentsObjectDTO;
import com.edu.icesi.conceptsreview.error.exception.AnimalError;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;


import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;


import static com.edu.icesi.conceptsreview.constant.AnimalsErrorCodes.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
public class AnimalIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    private void init()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void createAnimal() {
        String body = parseResourceToString("createAnimal.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isOk()).andReturn();

        AnimalDTO animalDTO = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);
        assertThat(animalDTO, hasProperty("name", is("NutriaPadre")));
    }

    @Test
    @SneakyThrows
    public void createAnimalRepeated() {
        String body = parseResourceToString("createAnimal.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest()).andReturn();

        AnimalDTO animalDTO = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);
        assertThat(animalDTO, hasProperty("name", is("NutriaPadre")));
    }

    @Test
    @SneakyThrows
    public void createAnimalBadNameLengthTest(){
        AnimalParentsDTO animalParentsDTO = createDummyAnimal();
        String name = StringUtils.repeat("M", 121);
        animalParentsDTO.setName(name);
        String body = objectMapper.writeValueAsString(animalParentsDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest()).andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_01)));
        assertThat(animalError,hasProperty("message",is(CODE_01.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalParentIDDoesNotExists(){
        AnimalParentsDTO animalParentsDTO = createDummyAnimal();
        animalParentsDTO.setFatherId(UUID.randomUUID());
        String body = objectMapper.writeValueAsString(animalParentsDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest()).andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_02)));
        assertThat(animalError,hasProperty("message",is(CODE_02.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalWrongParentGenre(){
        AnimalParentsDTO animalParentsDTO1 = createDummyAnimal();
        AnimalParentsDTO animalParentsDTO2 = createDummyAnimal();
        animalParentsDTO1.setFatherId(animalParentsDTO2.getId());

        String body = objectMapper.writeValueAsString(animalParentsDTO2);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest()).andReturn();

        body = objectMapper.writeValueAsString(animalParentsDTO1);
        result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest()).andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_04)));
        assertThat(animalError,hasProperty("message",is(CODE_04.getMessage())));
    }

    @SneakyThrows
    private String parseResourceToString(String classpath)
    {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        {
            return FileCopyUtils.copyToString(reader);
        }
    }

    private AnimalParentsDTO createDummyAnimal()
    {
        AnimalParentsDTO animalParentsDTO = new AnimalParentsDTO();
        animalParentsDTO.setName("Nutria");
        animalParentsDTO.setArriveDate(LocalDateTime.now());
        animalParentsDTO.setFatherId(null);
        animalParentsDTO.setMotherId(null);
        animalParentsDTO.setAge(10);
        animalParentsDTO.setHeight(10);
        animalParentsDTO.setId(UUID.randomUUID());
        animalParentsDTO.setGender("M");
        return animalParentsDTO;
    }

}
