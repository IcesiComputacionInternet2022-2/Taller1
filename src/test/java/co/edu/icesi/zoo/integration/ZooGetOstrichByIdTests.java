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
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooGetOstrichByIdTests {

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
    public void getOstrichById(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ostrich/ff97d807-22f1-4d81-a514-6a300fc64585")).andExpect(status().isOk()).andReturn();
        OstrichDTO ostrichResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichDTO.class);
        assertThat(ostrichResult, hasProperty("name", is("Small Ostrich")));
        assertThat(ostrichResult, hasProperty("gender", is(1)));
        assertThat(ostrichResult, hasProperty("weight", is(65.5f)));
        assertThat(ostrichResult, hasProperty("age", is(35)));
        assertThat(ostrichResult, hasProperty("height", is(1.7f)));
        assertThat(ostrichResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-13T12:00:00"))));
        assertThat(ostrichResult, hasProperty("fatherId", is(UUID.fromString("5afba3a1-96ba-46eb-b9c1-9ac690953fb4"))));
        assertThat(ostrichResult, hasProperty("motherId", is(UUID.fromString("59b99314-ed1a-4678-ab05-463b186c10c3"))));
    }

    @Test
    @SneakyThrows
    public void getOstrichByIdNotFound(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ostrich/ff97d807-22f1-4d81-a514-6a300fc64584")).andExpect(status().isNotFound()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_13.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_13.getMessage())));
    }

}
