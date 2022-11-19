package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.model.Animal;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class GetAnimalWithParentsIntegrationTest {

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
    public void getAnimalWithParents(){

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals/Nagini")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")).andExpect(status().isOk())
                .andReturn();

        AnimalWithParentsDTO animalWithParentsDTO = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalWithParentsDTO.class);

        assertNotNull(animalWithParentsDTO);
        assertTrue(animalWithParentsDTO instanceof AnimalWithParentsDTO);
        assertThat(animalWithParentsDTO, hasProperty(BurmesePython.NAME_ATTRIBUTE, is("Nagini")));
        assertThat(animalWithParentsDTO, hasProperty(BurmesePython.SEX_ATTRIBUTE, is('F')));
        assertThat(animalWithParentsDTO, hasProperty(BurmesePython.WEIGHT_ATTRIBUTE, is(5.5)));
        assertThat(animalWithParentsDTO, hasProperty(BurmesePython.HEIGHT_ATTRIBUTE, is(5.0)));
        assertThat(animalWithParentsDTO, hasProperty(BurmesePython.AGE_ATTRIBUTE, is(15.0)));
        assertThat(animalWithParentsDTO, hasProperty(BurmesePython.ARRIVAL_DATE_ATTRIBUTE, is("2003-11-20T00:00:00")));

        Animal mother = animalWithParentsDTO.getMotherInfo();

        assertNotNull(mother);
        assertTrue(mother instanceof Animal);
        assertThat(mother, hasProperty(BurmesePython.NAME_ATTRIBUTE, is("Satana")));
        assertThat(mother, hasProperty(BurmesePython.SEX_ATTRIBUTE, is('F')));
        assertThat(mother, hasProperty(BurmesePython.WEIGHT_ATTRIBUTE, is(5.5)));
        assertThat(mother, hasProperty(BurmesePython.HEIGHT_ATTRIBUTE, is(5.0)));
        assertThat(mother, hasProperty(BurmesePython.AGE_ATTRIBUTE, is(15.0)));
        assertEquals(mother.getArrivalDate().toString(), "2001-11-24T00:00");

        Animal father = animalWithParentsDTO.getFatherInfo();

        assertTrue(father instanceof Animal);
        assertThat(father, hasProperty(BurmesePython.NAME_ATTRIBUTE, is("Beelzebub")));
        assertThat(father, hasProperty(BurmesePython.SEX_ATTRIBUTE, is('M')));
        assertThat(father, hasProperty(BurmesePython.WEIGHT_ATTRIBUTE, is(1.5)));
        assertThat(father, hasProperty(BurmesePython.HEIGHT_ATTRIBUTE, is(0.1)));
        assertThat(father, hasProperty(BurmesePython.AGE_ATTRIBUTE, is(30.0)));
        assertEquals(father.getArrivalDate().toString(), "2004-12-31T00:00");;


 //IS ( '123e4567-e89b-42d3-a456-556642440000','Nagini', 'F', '5.5', '15', '5.0', '1998-11-20', '6c9e1336-13b8-4ecd-aa6f-f473c3c2e018', 'b55d9d91-2d6f-48f6-9442-8f654a0aba47')


    }
}
