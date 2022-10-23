package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.TatabroErrorCode;
import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.error.exception.TatabroError;
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
import java.time.LocalDateTime;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooGetTatabroByIDIntegrationTest {

    private static final UUID TATABRO_UUID_NON_EXISTENT = UUID.fromString("35a81e7d-342b-48e2-89e3-cccbb6e09f25");
    private static final UUID TATABRO_FATHER_UUID_EXISTENT = UUID.fromString("b55d9d91-2d6f-48f6-9442-8f654a0aba47");
    private static final UUID TATABRO_MOTHER_UUID_EXISTENT = UUID.fromString("5631cbd3-cf53-415f-bd06-4e995ee3c322");
    private static final UUID TATABRO_UUID_WITH_PARENTS = UUID.fromString("2f7908f1-c6c5-4fd9-bca9-07332d2c60e6");

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void getTatabroByIDWithoutParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/id/" + TATABRO_FATHER_UUID_EXISTENT).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        TatabroDTO tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroDTO.class);
        assertThat(tatabroResult, hasProperty("name", is("Tommy")));
        assertThat(tatabroResult, hasProperty("sex", is("M")));
        assertThat(tatabroResult, hasProperty("weight", is(24.0)));
        assertThat(tatabroResult, hasProperty("age", is(18)));
        assertThat(tatabroResult, hasProperty("height", is(53.0)));
        assertThat(tatabroResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2018-12-31T10:10:10"))));
        assertThat(tatabroResult, hasProperty("fatherID", nullValue()));
        assertThat(tatabroResult, hasProperty("motherID", nullValue()));
    }

    @Test
    @SneakyThrows
    public void getTatabroByIDWithParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/id/" + TATABRO_UUID_WITH_PARENTS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        TatabroDTO tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroDTO.class);
        assertThat(tatabroResult, hasProperty("name", is("Piggy")));
        assertThat(tatabroResult, hasProperty("sex", is("M")));
        assertThat(tatabroResult, hasProperty("weight", is(20.0)));
        assertThat(tatabroResult, hasProperty("age", is(15)));
        assertThat(tatabroResult, hasProperty("height", is(50.0)));
        assertThat(tatabroResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2021-12-13T11:13:19"))));
        assertThat(tatabroResult, hasProperty("fatherID", is(TATABRO_FATHER_UUID_EXISTENT)));
        assertThat(tatabroResult, hasProperty("motherID", is(TATABRO_MOTHER_UUID_EXISTENT)));
    }

    @Test
    @SneakyThrows
    public void getTatabroByIDNonExistent() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/id/" + TATABRO_UUID_NON_EXISTENT).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_01)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro ID doesn't exist.")));
    }
}