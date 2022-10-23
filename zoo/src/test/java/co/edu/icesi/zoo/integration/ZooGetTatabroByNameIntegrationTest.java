package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.TatabroErrorCode;
import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.dto.TatabroParentsDTO;
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
public class ZooGetTatabroByNameIntegrationTest {

    private static final String TATABRO_NAME_NON_EXISTENT = "Floppy";
    private static final TatabroDTO TATABRO_FATHER = new TatabroDTO(UUID.fromString("b55d9d91-2d6f-48f6-9442-8f654a0aba47"), "Tommy", "M", 24.0, 18, 53.0, LocalDateTime.parse("2018-12-31T10:10:10"), null, null);
    private static final TatabroDTO TATABRO_MOTHER = new TatabroDTO(UUID.fromString("5631cbd3-cf53-415f-bd06-4e995ee3c322"), "Dory", "F", 25.0, 17, 55.0, LocalDateTime.parse("2019-12-19T11:18:10"), null, null);
    private static final String TATABRO_NAME_WITH_PARENTS = "Piggy";

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
    public void getTatabroByNameWithoutParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/name/" + TATABRO_FATHER.getName()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        TatabroParentsDTO tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroParentsDTO.class);
        assertThat(tatabroResult, hasProperty("name", is("Tommy")));
        assertThat(tatabroResult, hasProperty("sex", is("M")));
        assertThat(tatabroResult, hasProperty("weight", is(24.0)));
        assertThat(tatabroResult, hasProperty("age", is(18)));
        assertThat(tatabroResult, hasProperty("height", is(53.0)));
        assertThat(tatabroResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2018-12-31T10:10:10"))));
        assertThat(tatabroResult, hasProperty("father", nullValue()));
        assertThat(tatabroResult, hasProperty("mother", nullValue()));
    }

    @Test
    @SneakyThrows
    public void getTatabroWithParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/name/" + TATABRO_NAME_WITH_PARENTS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        TatabroParentsDTO tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroParentsDTO.class);
        assertThat(tatabroResult, hasProperty("name", is("Piggy")));
        assertThat(tatabroResult, hasProperty("sex", is("M")));
        assertThat(tatabroResult, hasProperty("weight", is(20.0)));
        assertThat(tatabroResult, hasProperty("age", is(15)));
        assertThat(tatabroResult, hasProperty("height", is(50.0)));
        assertThat(tatabroResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2021-12-13T11:13:19"))));
        assertThat(tatabroResult, hasProperty("father", is(TATABRO_FATHER)));
        assertThat(tatabroResult, hasProperty("mother", is(TATABRO_MOTHER)));
    }

    @Test
    @SneakyThrows
    public void getTatabroNonExistent() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/name/" + TATABRO_NAME_NON_EXISTENT).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_02)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's name doesn't exist.")));
    }
}