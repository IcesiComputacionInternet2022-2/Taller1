package com.edu.icesi.restzooregisters.integration;

import com.edu.icesi.restzooregisters.dto.AnimalDTO;
import com.edu.icesi.restzooregisters.error.exception.AnimalError;
import com.edu.icesi.restzooregisters.error.exception.AnimalException;
import com.edu.icesi.restzooregisters.integration.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;

import static com.edu.icesi.restzooregisters.constants.AnimalErrorCode.*;
import static com.edu.icesi.restzooregisters.constants.TurtleCharacteristics.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
@Import({InitialDataConfig.class})
@ActiveProfiles("test")
public class CreateAnimalIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;




    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void createAnimal(){ //IS OK
        String body = parseResourceToString("createAnimal.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooregisters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalDTO= objectMapper.readValue(result.getResponse().getContentAsString(),AnimalDTO.class);

        assertThat(animalDTO,hasProperty("name",is("Perry")));
    }

    @Test
    @SneakyThrows
    public void createAnimalRepeated(){ //IS CONFLICT
        String body = parseResourceToString("createAnimal.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooregisters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isConflict())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_07)));
        assertThat(animalError,hasProperty("message",is(CODE_07.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testNameNoNumbers(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setName("1234");
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = getBadRequestResult(body);

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_02)));
        assertThat(animalError,hasProperty("message",is(CODE_02.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testNameLessThan120(){
        AnimalDTO baseAnimal = baseAnimal();
        String name = StringUtils.repeat("a", 121);
        baseAnimal.setName(name);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = getBadRequestResult(body);

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_02)));
        assertThat(animalError,hasProperty("message",is(CODE_02.getMessage())));
    }

    @SneakyThrows
    private MvcResult getBadRequestResult(String body){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooregisters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();
        return result;
    }

    @Test
    @SneakyThrows
    public void testValidateDate(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setArrivalDate(LocalDateTime.MAX);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = getBadRequestResult(body);
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_03)));
        assertThat(animalError,hasProperty("message",is(CODE_03.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testValidateAnimalHeight(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setHeight(MAX_HEIGHT+1);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = getBadRequestResult(body);
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_04)));
        assertThat(animalError,hasProperty("message",is(CODE_04.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testValidateAnimalWeight(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setWeight(MAX_WEIGHT+1);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = getBadRequestResult(body);
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_05)));
        assertThat(animalError,hasProperty("message",is(CODE_05.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testValidateAnimalAge(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setAge(MAX_AGE+1);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = getBadRequestResult(body);
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(CODE_06)));
        assertThat(animalError,hasProperty("message",is(CODE_06.getMessage())));
    }

    @SneakyThrows
    private String parseResourceToString(String classpath) {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    @SneakyThrows
    private AnimalDTO baseAnimal(){
        String body = parseResourceToString("createAnimal.json");
        return objectMapper.readValue(body, AnimalDTO.class);
    }

}
