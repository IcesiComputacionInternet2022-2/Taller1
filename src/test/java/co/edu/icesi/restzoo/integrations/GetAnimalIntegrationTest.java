package co.edu.icesi.restzoo.integrations;

import co.edu.icesi.restzoo.dto.AnimalDTO;
import co.edu.icesi.restzoo.integrations.config.InitialDataConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
public class GetAnimalIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    private final String GETTING_NAME = "Papa Oso";

    @SneakyThrows
    private MvcResult getStatusResult( String mapping) {
        return mockMvc.perform(MockMvcRequestBuilders.get(mapping)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void testGetAnimalById() { // Positive Outcome: Finds by ID
        String GETTING_ID = "af1a5314-bd9e-4529-85be-e1decade567a";
        MvcResult result = getStatusResult(String.format("/animals/id=%s", GETTING_ID));
        String response = result.getResponse().getContentAsString();
        AnimalDTO animal = objectMapper.readValue(response, AnimalDTO.class);

        assertThat(animal, hasProperty("id", is(UUID.fromString(GETTING_ID))));
    }

    @Test
    @SneakyThrows
    public void testGetAnimalByName() { // Positive Outcome: Finds by Name
        MvcResult result = getStatusResult(String.format("/animals/name=%s", GETTING_NAME));
        String response = result.getResponse().getContentAsString();
        AnimalDTO animal = objectMapper.readValue(response, AnimalDTO.class);

        assertThat(animal, hasProperty("name", is(GETTING_NAME)));
    }

    @Test
    @SneakyThrows
    public void testGetAnimals() { // Positive Outcome: Fetches all animals
        MvcResult result = getStatusResult("/animals");
        String response = result.getResponse().getContentAsString();
        List<AnimalDTO> animals = Arrays.stream(response.substring(1,response.length()-1)
                .split("},")).map(animal -> {
                    try {
                        return objectMapper.readValue(animal + "}", AnimalDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        assertThat(animals.get(0), hasProperty("name", is(GETTING_NAME)));
        assertThat(animals.get(1), hasProperty("name", is("Mama Oso")));
        assertThat(animals.get(2), hasProperty("name", is("Osito")));
    }
}
