package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.constant.OstrichErrorCode;
import co.edu.icesi.zoo.dto.OstrichDTO;
import co.edu.icesi.zoo.error.exception.OstrichError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooGetOstrichByNameTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void getOstrichByName(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ostrich/name/Small Ostrich")).andExpect(status().isOk()).andReturn();
        List<OstrichDTO> ostrichesResult = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), OstrichDTO[].class));
        assertThat(ostrichesResult.get(0), hasProperty("name", is("Small Ostrich")));
        assertThat(ostrichesResult.get(0), hasProperty("gender", is(1)));
        assertThat(ostrichesResult.get(0), hasProperty("weight", is(65.5f)));
        assertThat(ostrichesResult.get(0), hasProperty("age", is(35)));
        assertThat(ostrichesResult.get(0), hasProperty("height", is(1.7f)));
        assertThat(ostrichesResult.get(0), hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-13T12:00:00"))));
        assertThat(ostrichesResult.get(0), hasProperty("fatherId", is(UUID.fromString("5afba3a1-96ba-46eb-b9c1-9ac690953fb4"))));
        assertThat(ostrichesResult.get(0), hasProperty("motherId", is(UUID.fromString("59b99314-ed1a-4678-ab05-463b186c10c3"))));
        assertThat(ostrichesResult.get(1), hasProperty("name", is("Ostrich Father")));
        assertThat(ostrichesResult.get(1), hasProperty("gender", is(1)));
        assertThat(ostrichesResult.get(1), hasProperty("weight", is(65.5f)));
        assertThat(ostrichesResult.get(1), hasProperty("age", is(35)));
        assertThat(ostrichesResult.get(1), hasProperty("height", is(1.7f)));
        assertThat(ostrichesResult.get(1), hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-13T12:00:00"))));
        assertThat(ostrichesResult.get(1), hasProperty("fatherId", nullValue()));
        assertThat(ostrichesResult.get(1), hasProperty("motherId", nullValue()));
        assertThat(ostrichesResult.get(2), hasProperty("name", is("Ostrich Mother")));
        assertThat(ostrichesResult.get(2), hasProperty("gender", is(0)));
        assertThat(ostrichesResult.get(2), hasProperty("weight", is(65.5f)));
        assertThat(ostrichesResult.get(2), hasProperty("age", is(35)));
        assertThat(ostrichesResult.get(2), hasProperty("height", is(1.7f)));
        assertThat(ostrichesResult.get(2), hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-13T12:00:00"))));
        assertThat(ostrichesResult.get(2), hasProperty("fatherId", nullValue()));
        assertThat(ostrichesResult.get(2), hasProperty("motherId", nullValue()));
    }

    @Test
    @SneakyThrows
    public void getOstrichByNameNotFound(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ostrich/name/Medium Ostrich")).andExpect(status().isNotFound()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_13.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_13.getMessage())));
    }

}
