package co.edu.icesi.zoocanyonriver.integration;

import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.integration.config.InitialDataConfig;
import co.edu.icesi.zoocanyonriver.model.Tiger;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@Import({InitialDataConfig.class})
public class getTigersIntegrationTest {

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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tigers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        TigerDTO[] tigerResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDTO[].class);

        assertThat(tigerResult[0], hasProperty("id",is(UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e"))));
        assertThat(tigerResult[0], hasProperty("mother",nullValue()));
        assertThat(tigerResult[0], hasProperty("father",nullValue()));
        assertThat(tigerResult[0], hasProperty("name",is("Sanson")));
        assertThat(tigerResult[0], hasProperty("gender",is("Male")));
        assertThat(tigerResult[0], hasProperty("weight",is(200.0)));
        assertThat(tigerResult[0], hasProperty("age",is("5")));
        assertThat(tigerResult[0], hasProperty("height",is("130")));
        assertThat(tigerResult[0], hasProperty("arriveDate",is(LocalDateTime.parse("2021-12-13T15:15:00"))));

        assertThat(tigerResult[1], hasProperty("id",is(UUID.fromString("98d0c8af-3e20-4be9-8158-fc74c7b4d32d"))));
        assertThat(tigerResult[1], hasProperty("mother",nullValue()));
        assertThat(tigerResult[1], hasProperty("father",nullValue()));
        assertThat(tigerResult[1], hasProperty("name",is("Nala")));
        assertThat(tigerResult[1], hasProperty("gender",is("Female")));
        assertThat(tigerResult[1], hasProperty("weight",is(180.0)));
        assertThat(tigerResult[1], hasProperty("age",is("4")));
        assertThat(tigerResult[1], hasProperty("height",is("120")));
        assertThat(tigerResult[1], hasProperty("arriveDate",is(LocalDateTime.parse("2020-01-15T02:50:30"))));

        assertThat(tigerResult[2], hasProperty("id",is(UUID.fromString("999da34f-9e21-4392-b224-6185ee90ae40"))));
        assertThat(tigerResult[2], hasProperty("mother",is(tigerResult[2].getMother())));
        assertThat(tigerResult[2], hasProperty("father",is(tigerResult[2].getFather())));
        assertThat(tigerResult[2], hasProperty("name",is("Alex")));
        assertThat(tigerResult[2], hasProperty("gender",is("Male")));
        assertThat(tigerResult[2], hasProperty("weight",is(180.0)));
        assertThat(tigerResult[2], hasProperty("age",is("2")));
        assertThat(tigerResult[2], hasProperty("height",is("110")));
        assertThat(tigerResult[2], hasProperty("arriveDate",is(LocalDateTime.parse("2022-09-23T20:37:15"))));
    }

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
