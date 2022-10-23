package co.edu.icesi.ecozoo.integration;

import co.edu.icesi.ecozoo.config.jackson.LocalDateTimeDeserializer;
import co.edu.icesi.ecozoo.config.jackson.LocalDateTimeSerializer;
import co.edu.icesi.ecozoo.constant.AnimalErrorCode;
import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.constant.CapybaraConstraints;
import co.edu.icesi.ecozoo.dto.AnimalResponseDTO;
import co.edu.icesi.ecozoo.dto.CapybaraDTO;
import co.edu.icesi.ecozoo.error.exception.AnimalError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class AnimalIntegrationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    private static final String ANIMAL_UUID = "5631cbd3-cf53-415f-bd06-4e995ee3c322";


    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(module);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @SneakyThrows
    public void createAnimalSuccessfully() {
        CapybaraDTO baseAnimal = baseAnimal();
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        CapybaraDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), CapybaraDTO.class);

    }

    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithWrongName(){
        CapybaraDTO baseAnimal = baseAnimal();
        baseAnimal.setName("@143asd");
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The name of the animal can only contain letters")));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithoutName(){
        CapybaraDTO baseAnimal = baseAnimal();
        baseAnimal.setName(null);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The name of the animal cannot be null")));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithLongerName(){
        CapybaraDTO baseAnimal = baseAnimal();
        String wrongName = "a".repeat(121);
        baseAnimal.setName(wrongName);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The name of the animal must be between 1 and 120 characters")));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }
    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithoutSex(){
        CapybaraDTO baseAnimal = baseAnimal();
        baseAnimal.setSex(null);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The sex of the animal cannot be null")));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithWrongWeight(){
        CapybaraDTO baseAnimal = baseAnimal();
        baseAnimal.setWeight(Double.parseDouble(CapybaraConstraints.MAX_WEIGHT)+1);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The weight of the animal must be less or equal than "+ CapybaraConstraints.MAX_WEIGHT)));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithWrongHeight(){
        CapybaraDTO baseAnimal = baseAnimal();
        baseAnimal.setHeight(Double.parseDouble(CapybaraConstraints.MIN_HEIGHT)-1);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The height of the animal must be greater or equal than "+ CapybaraConstraints.MIN_HEIGHT)));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    void createAnimalUnsuccessfullyWithWrongAge(){
        CapybaraDTO baseAnimal = baseAnimal();
        baseAnimal.setAge(CapybaraConstraints.MAX_AGE+1);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is("The age of the animal must be between " + CapybaraConstraints.MIN_AGE + " and " + CapybaraConstraints.MAX_AGE)));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    public void getAnimalSuccessfully() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/capybaras/" + ANIMAL_UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AnimalResponseDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalResponseDTO.class);

        assertThat(animalResult, hasProperty("name", is("Shirohige")));
        assertThat(animalResult, hasProperty("sex", is(AnimalSex.MALE)));
        assertThat(animalResult, hasProperty("weight", is(1.5)));
        assertThat(animalResult, hasProperty("age", is(10)));
        assertThat(animalResult, hasProperty("height", is(3.5)));
        assertThat(animalResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2020-10-09T10:30", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))));
    }

    @Test
    @SneakyThrows
    public void getAnimalUnSuccessfullyNotFound() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/capybaras/" + ANIMAL_UUID.replace('0','1'))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        AnimalError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(err, hasProperty("message", is(AnimalErrorCode.CODE_01.getMessage())));
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_01)));

    }

    @Test
    @SneakyThrows
    public void getAnimalsSuccessfully() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/capybaras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }


    @SneakyThrows
    private CapybaraDTO baseAnimal() {
        String body = parseResourceToString("animal.json");
        return objectMapper.readValue(body, CapybaraDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }


}