package co.edu.icesi.calizoo.integration;


import co.edu.icesi.calizoo.constant.HyenaErrorCode;
import co.edu.icesi.calizoo.dto.HyenaDTO;
import co.edu.icesi.calizoo.error.exception.HyenaError;
import co.edu.icesi.calizoo.integration.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.datasource.url=jdbc:h2:mem:testdb" }
)
@ActiveProfiles("test")

public class HyenaIntegrationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    private static final String HYENA_UUID = "5631cbd3-cf53-415f-bd06-4e995ee3c322";


    @BeforeEach
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @Test
    @SneakyThrows
    public void createHyenaSuccessfully() {
        String body = parseResourceToString("createHyena.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)).andExpect(status().isOk())
                    .andReturn();

        HyenaDTO hyenaResult = objectMapper.readValue(result.getResponse().getContentAsString(),HyenaDTO.class);

        assertThat(hyenaResult,hasProperty("name",is("Alex")));

    }

    @Test
    @SneakyThrows
    public void getHyenaSuccessfully() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/hyenas/" + HYENA_UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        HyenaDTO hyenaResult = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaDTO.class);

        assertThat(hyenaResult, hasProperty("name", Matchers.is("Abby")));

    }

    @Test
    @SneakyThrows
    public void createHyenaWithWrongName() {
        String body = parseResourceToString("createHyenaWithWrongName.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code", is(HyenaErrorCode.CODE_01)));
        assertThat(hyenaError, hasProperty("message", is("Name should contains only letters and spaces. It should be between 1 and 120 characters long")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWithArrivalDateAfterToday() {
        String body = parseResourceToString("createHyenaWithArrivalDateAfterToday.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code", is(HyenaErrorCode.CODE_02)));
        assertThat(hyenaError, hasProperty("message", is("The arrival date should be before the current date")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWithWrongAge() {
        String body = parseResourceToString("createHyenaWithWrongAge.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code", is(HyenaErrorCode.CODE_03)));
        assertThat(hyenaError, hasProperty("message", is("Age should be between 1 and 25 years old according to hyenas aging. Please refer to wikipedia for more information")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWithWrongWeight() {
        String body = parseResourceToString("createHyenaWithWrongWeight.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code",is(HyenaErrorCode.CODE_04)));
        assertThat(hyenaError, hasProperty("message", is("Weight should be between 1 and 64 kg (kilograms according to hyenas weighting. Please refer to wikipedia for more information")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWithWrongHeight() {
        String body = parseResourceToString("createHyenaWithWrongHeight.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code",is(HyenaErrorCode.CODE_05)));
        assertThat(hyenaError, hasProperty("message", is("Height should be between 1 and 92 cm (centimeters) according to hyenas general height. Please refer to wikipedia for more information")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWithWrongFather() {
        String body = parseResourceToString("createHyenaWithWrongFather.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code",is(HyenaErrorCode.CODE_06)));
        assertThat(hyenaError, hasProperty("message", is("Father gender should be male")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWithWrongMother() {
        String body = parseResourceToString("createHyenaWithWrongMother.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code",is(HyenaErrorCode.CODE_07)));
        assertThat(hyenaError, hasProperty("message", is("Mother gender should be female")));
    }

    @Test
    @SneakyThrows
    public void createHyenaWihTakenName() {
        String body = parseResourceToString("createHyenaWithTakenName.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        HyenaError hyenaError = objectMapper.readValue(result.getResponse().getContentAsString(), HyenaError.class);

        assertThat(hyenaError, hasProperty("code",is(HyenaErrorCode.CODE_08)));
        assertThat(hyenaError, hasProperty("message", is("Name is already taken")));
    }

    @SneakyThrows
    private String parseResourceToString (String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(),UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
