package co.icesi.edu.animals.integration;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({InitialDataConfig.class})
public class AnimalsGetCaripiareAndParentsIntegrationTest {

    private static final String CHILDREN_CARIPIARE_NAME = "children";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void testGetCaripiareAndParents(){
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/caripiares/name/" + CHILDREN_CARIPIARE_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CaripiareAndParentsDTO caripiareAndParentsDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareAndParentsDTO.class);
        assertThat(caripiareAndParentsDTO, hasProperty("name", is(CHILDREN_CARIPIARE_NAME)));
        assertThat(caripiareAndParentsDTO, hasProperty("father", is(notNullValue())));
        assertThat(caripiareAndParentsDTO, hasProperty("mother", is(notNullValue())));
    }

    @Test
    @SneakyThrows
    public void testGetCaripiareAndParentsNotFound() {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/caripiares/name/" + "NonExistentCaripiare")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        CaripiareError caripiareError = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareError.class);
        assertThat(caripiareError, hasProperty("caripiareErrorCode", is(CaripiareErrorCode.CODE_02)));
    }
}
