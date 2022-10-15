package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.AnimalGender;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.integration.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({InitialDataConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AnimalGetAnimalIntegrationTest {

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

    @Test
    @SneakyThrows
    public void getAnimalWithoutParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals/{animalName}", "Luna")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        AnimalWithParentsDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalWithParentsDTO.class);

        assertThat(animalResult, hasProperty("name", is("Luna")));
        assertThat(animalResult, hasProperty("sex", is(AnimalGender.F)));
        assertThat(animalResult, hasProperty("age", is(10)));
        assertThat(animalResult, hasProperty("height", is(2.0)));
        assertThat(animalResult, hasProperty("weight", is(150.0)));
    }

    @Test
    @SneakyThrows
    public void getAnimalWithParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals/{animalName}", "Nicol")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();

        AnimalWithParentsDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalWithParentsDTO.class);

        assertThat(animalResult, hasProperty("name", is("Nicol")));
        assertThat(animalResult, hasProperty("sex", is(AnimalGender.F)));
        assertThat(animalResult, hasProperty("age", is(10)));
        assertThat(animalResult, hasProperty("height", is(2.0)));
        assertThat(animalResult, hasProperty("weight", is(150.0)));
        assertThat(animalResult, hasProperty("mother", hasProperty("name", is("Luna"))));
        assertThat(animalResult, hasProperty("father", hasProperty("name", is("Pep"))));
    }
}
