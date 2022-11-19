package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.constant.AnimalTestConstants;
import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.error.exception.AnimalError;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class CreateAnimalIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @SneakyThrows
    @Test
    public void createAnimalTest(){

        AnimalDTO animal = baseAnimal();
        animal.setName("Joshua");
        String body = objectMapper.writeValueAsString(animal);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AnimalDTO animalDTO = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalDTO.class);

        assertNotNull(animalDTO);
        assertTrue(animalDTO instanceof AnimalDTO);
        assertThat(animalDTO, hasProperty(BurmesePython.NAME_ATTRIBUTE, is(AnimalTestConstants.ANIMAL_TEST_NAME)));
        assertThat(animalDTO, hasProperty(BurmesePython.SEX_ATTRIBUTE, is(AnimalTestConstants.ANIMAL_TEST_SEX)));
        assertThat(animalDTO, hasProperty(BurmesePython.WEIGHT_ATTRIBUTE, is(AnimalTestConstants.ANIMAL_TEST_WEIGHT)));
        assertThat(animalDTO, hasProperty(BurmesePython.HEIGHT_ATTRIBUTE, is(AnimalTestConstants.ANIMAL_TEST_HEIGHT)));
        assertThat(animalDTO, hasProperty(BurmesePython.AGE_ATTRIBUTE, is(AnimalTestConstants.ANIMAL_TEST_AGE)));
        assertThat(animalDTO, hasProperty(BurmesePython.ARRIVAL_DATE_ATTRIBUTE, is(AnimalTestConstants.ANIMAL_TEST_ARRIVAL_DATE)));
        assertThat(animalDTO, hasProperty(BurmesePython.MOTHER_ID, is(AnimalTestConstants.ANIMAL_TEST_MOTHER_ID)));
        assertThat(animalDTO, hasProperty(BurmesePython.FATHER_ID, is(AnimalTestConstants.ANIMAL_TEST_FATHER_ID)));

    }

    /*
     * CONTROLLER RESPONSIBILITY
     */
    @SneakyThrows
    @Test
    public void createAnimalInvalidNameFormatTest(){

        //Set an invalid name format
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setName(animalDTO.getName()+"-Burmese");
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_01, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalInvalidNameLengthTest(){

        //Set an invalid name length
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setName("Asmodeussssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_01, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalImpossibleArrivalDateTest(){

        long futureArrivalTime = System.currentTimeMillis() + 86400000L;
        LocalDateTime futureArrivalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(futureArrivalTime), ZoneId.systemDefault());

        //Set an invalid arrival date
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setArrivalDate(futureArrivalDate.toString());
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_02_01, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalInvalidDateFormatTest(){

        //Set an invalid arrival date format (not ISO 8601)
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setArrivalDate("2022.10.27 14:15pm");
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_02_02, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalInvalidAgeTest(){

        //Set an invalid age (age>30)
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setAge(31.0);
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_03, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalInvalidHeightTest(){

        //Set an invalid height (height>8)
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setHeight(9.0);
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_03, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalInvalidWeightTest(){

        //Set an invalid weight (weight>180)
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setWeight(181.0);
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_03, animalError);

    }

    @SneakyThrows
    @Test
    public void createAnimalInvalidParentUUIDFormat(){

        //Set an invalid weight (weight>180)
        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setMotherId("a00347293");
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_06, animalError);

    }

    /*
     * SERVICE RESPONSIBILITY
     */
    @SneakyThrows
    @Test
    public void createAnimalRepeatedNameTest(){

        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setName("Beelzebub");
        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_04, animalError);

    }

    @SneakyThrows
    @Test
    public void wrongMotherSexTest(){


        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setMotherId(AnimalTestConstants.ANIMAL_TEST_FATHER_ID);
        animalDTO.setFatherId(null);


        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_05_01, animalError);


    }

    @SneakyThrows
    @Test
    public void wrongFatherSexTest(){

        AnimalDTO animalDTO = baseAnimal();
        animalDTO.setFatherId(AnimalTestConstants.ANIMAL_TEST_MOTHER_ID);
        animalDTO.setMotherId(null);


        String body = objectMapper.writeValueAsString(animalDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        verifyAnimalError( AnimalErrorCode.CODE_05_02, animalError);


    }



    //'b55d9d91-2d6f-48f6-9442-8f654a0aba47','Beelzebub', 'M'
    //'6c9e1336-13b8-4ecd-aa6f-f473c3c2e018','Satana', 'F',



    /*
     * UTILS
     */
    @SneakyThrows
    private AnimalDTO baseAnimal(){
        String body = parseResourceToString("createAnimal.json");
        return objectMapper.readValue(body, AnimalDTO.class);
    }
    @SneakyThrows
    private String parseResourceToString(String classpath) {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    public void verifyAnimalError( AnimalErrorCode correctCode, AnimalError animalError) {

        assertNotNull(animalError);
        assertEquals(correctCode.getMessage(), animalError.getMessage());
        assertEquals(correctCode, animalError.getCode());

    }


}
