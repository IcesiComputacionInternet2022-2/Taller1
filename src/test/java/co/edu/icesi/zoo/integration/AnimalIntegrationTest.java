package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.dto.AnimalDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AnimalIntegrationTest {

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
    private void firstScenario() {
        mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parseResourceToString("createFemaleAnimal.json"))).andExpect(status().isOk())
                .andReturn();
    }

    @SneakyThrows
    private void secondScenario() {
        mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parseResourceToString("createMaleAnimal.json"))).andExpect(status().isOk())
                .andReturn();
    }

    @SneakyThrows
    private void thirdScenario() {
        firstScenario();
        secondScenario();
    }

    @Test
    @SneakyThrows
    public void createAnimalSuccessfully() {
        String body = parseResourceToString("createFemaleAnimal.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("name", is("Luna")));
    }

    @Test
    @SneakyThrows
    public void createAnimalWithMotherSuccessfully() {
        firstScenario();

        String body = parseResourceToString("createAnimalWithMother.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("mother", is("Luna")));
    }

    @Test
    @SneakyThrows
    public void createAnimalWithFatherSuccessfully() {
        secondScenario();

        String body = parseResourceToString("createAnimalWithFather.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("father", is("Oscar")));
    }

    @Test
    @SneakyThrows
    public void createAnimalWithParentsSuccessfully() {
        thirdScenario();

        String body = parseResourceToString("createAnimalWithParents.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("father", is("Oscar")));
        assertThat(animalResult, hasProperty("mother", is("Luna")));
    }

    @SneakyThrows
    private String parseResourceToString(String classpath) {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
