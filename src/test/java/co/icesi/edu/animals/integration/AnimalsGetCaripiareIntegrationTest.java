package co.icesi.edu.animals.integration;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import co.icesi.edu.animals.dto.CaripiareDTO;
import co.icesi.edu.animals.error.exception.CaripiareError;
import co.icesi.edu.animals.integration.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({InitialDataConfig.class})
public class AnimalsGetCaripiareIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    private static final UUID CHILDREN_CARIPIARE_UUID = UUID.fromString("502807ed-21f1-4b69-a8b6-0c365cafe117");

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void testGetCaripiare(){
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/caripiares/id/" + CHILDREN_CARIPIARE_UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CaripiareDTO caripiareDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareDTO.class);
        assertThat(caripiareDTO, hasProperty("id", is(CHILDREN_CARIPIARE_UUID)));
        assertThat(caripiareDTO, hasProperty("name", is("children")));
        assertThat(caripiareDTO, hasProperty("gender", is("M")));
        assertThat(caripiareDTO, hasProperty("weight", is(3.5)));
        assertThat(caripiareDTO, hasProperty("age", is(9)));
        assertThat(caripiareDTO, hasProperty("height", is(0.15)));
        assertThat(caripiareDTO.getArrivalDate(), is(LocalDate.of(2022, 8, 24)));
    }

    @Test
    @SneakyThrows
    public void testGetCaripiareNotFound() {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/caripiares/id/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        CaripiareError caripiareError = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareError.class);
        assertThat(caripiareError, hasProperty("caripiareErrorCode", is(CaripiareErrorCode.CODE_02)));
    }
}
