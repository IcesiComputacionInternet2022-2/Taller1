package co.edu.icesi.zoocanyonriver.integration;

import co.edu.icesi.zoocanyonriver.constants.CodesError;
import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.dto.TigerResponseDTO;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class getTigerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    private static final String NAME_TIGER = "Nala";
    private static final String NAME_TIGER_WHIT_PARENTS = "Alex";
    private static final String NAME_TIGER_FALSE = "HALAND";

    private static final String TIGER_WHIT_FALSE_PARENT = "Jairo";

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void getTigerSuccessfully() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tigers/" + NAME_TIGER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TigerResponseDTO tigerResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerResponseDTO.class);

        assertThat(tigerResult, hasProperty("id", is(UUID.fromString("98d0c8af-3e20-4be9-8158-fc74c7b4d32d"))));
    }

    @Test
    @SneakyThrows
    public void getTigerUnsuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tigers/" + NAME_TIGER_FALSE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);

        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_03.getMessage())));
    }

    @Test
    @SneakyThrows
    public void getTigerIDWhitParentsSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tigers/" + NAME_TIGER_WHIT_PARENTS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TigerResponseDTO tigerResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerResponseDTO.class);
        assertThat(tigerResult, hasProperty("id", is(UUID.fromString("999da34f-9e21-4392-b224-6185ee90ae40"))));
        assertThat(tigerResult, hasProperty("mother", is(tigerResult.getMother())));
        assertThat(tigerResult, hasProperty("father", is(tigerResult.getFather())));
        assertThat(tigerResult, hasProperty("name", is("Alex")));
        assertThat(tigerResult, hasProperty("gender", is("Male")));
        assertThat(tigerResult, hasProperty("weight", is(180.0)));
        assertThat(tigerResult, hasProperty("age", is("2")));
        assertThat(tigerResult, hasProperty("height", is("110")));
        assertThat(tigerResult, hasProperty("arriveDate", is(LocalDateTime.parse("2022-09-23T20:37:15"))));
    }

    @Test
    @SneakyThrows
    public void getTigerIDWhitFalseParentSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tigers/" + TIGER_WHIT_FALSE_PARENT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        TigerDemoError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), TigerDemoError.class);
        assertThat(exceptionResult, hasProperty("code", is(CodesError.CODE_04.getCode())));
        assertThat(exceptionResult, hasProperty("message", is(CodesError.CODE_04.getMessage())));
    }

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
