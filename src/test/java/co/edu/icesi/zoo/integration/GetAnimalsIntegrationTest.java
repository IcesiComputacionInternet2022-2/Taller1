package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.AnimalTestConstants;
import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class GetAnimalsIntegrationTest {

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
    public void getAnimalsTest(){

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")).andExpect(status().isOk())
                .andReturn();

        AnimalNoParentsDTO[] animalNoParentsDTOList = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalNoParentsDTO[].class);
        AnimalNoParentsDTO beelzebub = animalNoParentsDTOList[0];
        AnimalNoParentsDTO satana = animalNoParentsDTOList[1];

        assertNotNull(beelzebub);
        assertTrue(beelzebub instanceof AnimalNoParentsDTO);
        assertThat(beelzebub, hasProperty(BurmesePython.NAME_ATTRIBUTE, is("Beelzebub")));
        assertThat(beelzebub, hasProperty(BurmesePython.SEX_ATTRIBUTE, is('M')));
        assertThat(beelzebub, hasProperty(BurmesePython.WEIGHT_ATTRIBUTE, is(1.5)));
        assertThat(beelzebub, hasProperty(BurmesePython.HEIGHT_ATTRIBUTE, is(0.1)));
        assertThat(beelzebub, hasProperty(BurmesePython.AGE_ATTRIBUTE, is(30.0)));
        assertThat(beelzebub, hasProperty(BurmesePython.ARRIVAL_DATE_ATTRIBUTE, is("2004-12-31T00:00:00")));

        assertNotNull(satana);
        assertTrue(satana instanceof AnimalNoParentsDTO);
        assertThat(satana, hasProperty(BurmesePython.NAME_ATTRIBUTE, is("Satana")));
        assertThat(satana, hasProperty(BurmesePython.SEX_ATTRIBUTE, is('F')));
        assertThat(satana, hasProperty(BurmesePython.WEIGHT_ATTRIBUTE, is(5.5)));
        assertThat(satana, hasProperty(BurmesePython.HEIGHT_ATTRIBUTE, is(5.0)));
        assertThat(satana, hasProperty(BurmesePython.AGE_ATTRIBUTE, is(15.0)));
        assertThat(satana, hasProperty(BurmesePython.ARRIVAL_DATE_ATTRIBUTE, is("2001-11-24T00:00:00")));

    }
}
