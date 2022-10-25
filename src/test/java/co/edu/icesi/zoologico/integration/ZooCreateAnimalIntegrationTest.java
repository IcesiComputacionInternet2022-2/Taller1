package co.edu.icesi.zoologico.integration;

import co.edu.icesi.zoologico.config.jackson.LocalDateTimeDeserializer;
import co.edu.icesi.zoologico.config.jackson.LocalDateTimeSerializer;
import co.edu.icesi.zoologico.constant.AnimalErrorCode;
import co.edu.icesi.zoologico.dto.AnimalDTO;
import co.edu.icesi.zoologico.error.exception.AnimalDemoError;
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
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooCreateAnimalIntegrationTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;



    @BeforeEach
    private void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();


        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(module);

    }

    @Test
    @SneakyThrows
    public void createAnimalSuccessfullyTest() {
        AnimalDTO animal = baseAnimal();
        animal.setName("Antonia Kill");
        String body = objectMapper.writeValueAsString(animal);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        AnimalDTO animalResult = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalResult, hasProperty("name", is("Antonia Kill")));
    }



    @Test
    @SneakyThrows
    void createAnimalWrongNameTest(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setName("gustavo.villada");
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_01)));
    }


    @Test
    @SneakyThrows
    void createAnimalWrongHeightTest(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setHeight(150);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_03)));
    }


    @Test
    @SneakyThrows
    void createAnimalWrongWeightTest(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setWeight(150);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_04)));
    }

    @Test
    @SneakyThrows
    void createAnimalWrongAgeTest(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setAge(150);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_05)));
    }

    @Test
    @SneakyThrows
    void createAnimalNameRepeatedTest(){
        AnimalDTO baseAnimal = baseAnimal();
        baseAnimal.setName("Gustavo");
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_06)));
    }

    @Test
    @SneakyThrows
    void createAnimalAnimalMotherDoesntExistTest(){
        AnimalDTO baseAnimal = baseAnimal();
        UUID uuidMotherInExistent=UUID.randomUUID();

        baseAnimal.setMother(uuidMotherInExistent);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_07)));
    }


    @Test
    @SneakyThrows
    void createAnimalFatherDoesntExistTest(){
        AnimalDTO baseAnimal = baseAnimal();
        UUID uuidFatherInExistent=UUID.randomUUID();

        baseAnimal.setFather(uuidFatherInExistent);
        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_09)));
    }

    @Test
    @SneakyThrows
    void createAnimalFatherIsFemaleTest(){
        AnimalDTO baseAnimal = baseAnimal();
        UUID animalFatherUUID= UUID.fromString ("c99d6dae-b0a2-4648-ad54-bf103621ed7a");
        baseAnimal.setFather(animalFatherUUID);

        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_10)));
    }


    @Test
    @SneakyThrows
    void createAnimalMotherIsMaleTest(){
        AnimalDTO baseAnimal = baseAnimal();
        UUID animalMotherUUID= UUID.fromString ("b17e9f5b-77f4-4bdf-bc78-21fecddc8526");
        baseAnimal.setMother(animalMotherUUID);

        String body = objectMapper.writeValueAsString(baseAnimal);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalDemoError err = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDemoError.class);
        assertThat(err, hasProperty("code", is(AnimalErrorCode.CODE_08)));
    }
    /*
     * UTILS
     */
    @SneakyThrows
    private AnimalDTO baseAnimal() {
        String body = parseResourceToString("createAnimal.json");
        return objectMapper.readValue(body, AnimalDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }


}
