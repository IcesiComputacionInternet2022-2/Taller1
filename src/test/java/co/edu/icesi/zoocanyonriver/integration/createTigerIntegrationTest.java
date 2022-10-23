package co.edu.icesi.zoocanyonriver.integration;

import co.edu.icesi.zoocanyonriver.constants.CodesError;
import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoError;
import co.edu.icesi.zoocanyonriver.integration.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@Import({InitialDataConfig.class})
public class createTigerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void createTigerSuccesfully() {
        String body = parseResourceToString("createTiger.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        TigerDTO tigerResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDTO.class);

        assertTrue(tigerResult instanceof TigerDTO);
        assertThat(tigerResult, hasProperty("mother", nullValue()));
        assertThat(tigerResult, hasProperty("father", nullValue()));
        assertThat(tigerResult, hasProperty("name", is("Loki")));
        assertThat(tigerResult, hasProperty("gender", is("Male")));
        assertThat(tigerResult, hasProperty("weight", is(290.0)));
        assertThat(tigerResult, hasProperty("age", is("8")));
        assertThat(tigerResult, hasProperty("height", is("120")));
        assertThat(tigerResult, hasProperty("arriveDate", is(LocalDateTime.parse("2021-06-10T20:43:00"))));
    }

    @Test
    @SneakyThrows
    public void createTigerWithRepeatName() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setName("Sanson");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_12.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_12.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyMotherExistence() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setMother("787c9ce3-a29c-49ff-96b0-48de2a335189");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_13.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_13.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyFatherExistence() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setFather("787c9ce3-a29c-49ff-96b0-48de2a335189");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_14.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_14.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyMotherGenreExistence() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setMother("c30f50a0-b01b-4770-b228-12eb0603506e");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_15.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_15.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyFatherGenreExistence() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setFather("98d0c8af-3e20-4be9-8158-fc74c7b4d32d");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_16.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_16.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyHeight() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setHeight("130");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_09.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_09.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyAge() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setAge("30");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_08.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_08.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyGender() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setGender("Person");
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_07.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_07.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyWeightMale() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setName("invalid tiger");
        baseTiger.setGender("Male");
        baseTiger.setWeight(320);
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_05.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_05.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyWeightFemale() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setName("tigerInvalid");
        baseTiger.setGender("Female");
        baseTiger.setWeight(300);
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_06.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_06.getMessage())));
    }

    @Test
    @SneakyThrows
    public void verifyDate() {
        TigerDTO baseTiger = baseTiger();
        baseTiger.setArriveDate(LocalDateTime.parse("2023-01-20T05:50:53"));
        String body = objectMapper.writeValueAsString(baseTiger);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_11.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_11.getMessage())));
    }


    @SneakyThrows
    private TigerDTO baseTiger() {
        String body = parseResourceToString("createTiger.json");
        return objectMapper.readValue(body, TigerDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
