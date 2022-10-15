package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.constant.AnimalGender;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.error.exception.AnimalError;
import co.edu.icesi.zoo.integration.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({InitialDataConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AnimalCreateAnimalIntegrationTest {

    private final static String FEMALE_ANIMAL_NAME = "Luna";
    private final static String MALE_ANIMAL_NAME = "Pep";

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @SneakyThrows
    private AnimalDTO baseAnimal() {
        String body = parseResourceToString("json/createAnimal.json");
        return objectMapper.readValue(body, AnimalDTO.class);
    }

    @Test
    @SneakyThrows
    public void createAnimalSuccessfully() {
        AnimalDTO baseAnimal = baseAnimal();
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("name", is("Lupe")));
        assertThat(animalResult, hasProperty("sex", is(AnimalGender.F)));
        assertThat(animalResult, hasProperty("age", is(10)));
        assertThat(animalResult, hasProperty("height", is(2.0)));
        assertThat(animalResult, hasProperty("weight", is(150.0)));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorFieldNull() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setName(null);
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_01)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_01.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorFatherFemale() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setFather(FEMALE_ANIMAL_NAME);
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_02)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_02.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorMotherMale() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setMother(MALE_ANIMAL_NAME);
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_03)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_03.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorNameLength() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setName("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_04)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_04.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorParentNonexistent() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setMother("MotherToFail");
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_05)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_05.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorNameSpecialCharacters() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setName("@NameToFail");
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_06)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_06.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorWeight() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setWeight(1000);
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_07)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_07.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorHeight() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setHeight(1000);
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_08)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_08.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalErrorArrivalDateFromFuture() {
        AnimalDTO animalToFail = baseAnimal();
        animalToFail.setArrivalDate(LocalDateTime.of(3000, 10, 10, 0, 0, 0));
        String body = objectMapper.writeValueAsString(animalToFail);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);

        assertThat(animalError, hasProperty("code", is(AnimalErrorCode.CODE_09)));
        assertThat(animalError, hasProperty("message", is(AnimalErrorCode.CODE_09.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createAnimalWithParentsSuccessfully() {
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setMother(FEMALE_ANIMAL_NAME);
        baseAnimal.setFather(MALE_ANIMAL_NAME);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("name", is("Lupe")));
        assertThat(animalResult, hasProperty("sex", is(AnimalGender.F)));
        assertThat(animalResult, hasProperty("age", is(10)));
        assertThat(animalResult, hasProperty("height", is(2.0)));
        assertThat(animalResult, hasProperty("weight", is(150.0)));
        assertThat(animalResult, hasProperty("mother", is(FEMALE_ANIMAL_NAME)));
        assertThat(animalResult, hasProperty("father", is(MALE_ANIMAL_NAME)));
    }

    @SneakyThrows
    private String parseResourceToString(String classpath) {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
