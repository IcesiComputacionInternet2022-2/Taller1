package co.edu.icesi.restzoo.integrations;

import co.edu.icesi.restzoo.constant.AnimalErrorCode;
import co.edu.icesi.restzoo.constant.Constants;
import co.edu.icesi.restzoo.dto.AnimalDTO;
import co.edu.icesi.restzoo.error.exception.AnimalError;
import co.edu.icesi.restzoo.integrations.config.InitialDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.maven.shared.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
@Import({InitialDataConfig.class})
@ActiveProfiles("test")
public class CreateAnimalIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    private String parseResourceToString() {
        Resource resource = new ClassPathResource("createAnimal.json");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    @SneakyThrows
    private AnimalDTO setupAnimalDTO() {
        String body = parseResourceToString();
        return objectMapper.readValue(body, AnimalDTO.class);
    }

    @SneakyThrows
    private MvcResult getStatusResult(String body, String stat) {
        return mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(matchToStatus(stat))
                .andReturn();
    }

    @SneakyThrows
    private ResultMatcher matchToStatus(String stat) {
        switch (stat) {
            case "OK":
                return status().isOk();
            case "NOT_FOUND":
                return status().isNotFound();
            case "CONFLICT":
                return status().isConflict();
            case "BAD_REQUEST":
                return status().isBadRequest();
            default:
                return status().isIAmATeapot();
        }
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void testCreateAnimal() { // Positive Outcome
        String body = parseResourceToString();
        MvcResult result = getStatusResult(body, "OK");
        AnimalDTO animalDTO = objectMapper.readValue(result.getResponse().getContentAsString(),AnimalDTO.class);
        assertThat(animalDTO,hasProperty("name",is("Aardvark")));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalConflict() { // Negative Outcome: Repeated name
        String body = parseResourceToString();
        MvcResult result = getStatusResult(body, "OK"); // Create once
        result = getStatusResult(body, "CONFLICT"); // Create twice
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.SER_E0x01)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.SER_E0x01.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalNameContent() { // Negative Outcome: Name contains numbers
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setName("247685"); // Number row keyboard smash let's go (it's past midnight don't judge me too hard)
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x11)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x11.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalNameLength() { // Negative Outcome: Name too long
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setName(StringUtils.repeat("E", 121));
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x12)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x12.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalDateFuture() { // Negative Outcome: Date is in the future
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setArrivalDate(LocalDateTime.MAX);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.SER_E0x04)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.SER_E0x04.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalTooLight() { // Negative Outcome: Weight too little
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setWeight(Double.parseDouble(Constants.MIN_HEALTHY_WEIGHT.getValue()) - 1);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x15_1)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x15_1.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalTooHeavy() { // Negative Outcome: Weight too much
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setWeight(Double.parseDouble(Constants.MAX_HEALTHY_WEIGHT.getValue()) + 1);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x15_2)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x15_2.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalTooOld() { // Negative Outcome: Age too much
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setAge(Double.parseDouble(Constants.MAX_LONGEVITY.getValue()) + 1);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x14)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x14.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalNegativeAge() { // Negative Outcome: Age is negative
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setAge(-1);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x14)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x14.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalTooShort() { // Negative Outcome: Length too little
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setLength(Double.parseDouble(Constants.MIN_BABY_LENGTH.getValue()) - 1);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x16_1)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x16_1.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCreateAnimalTooLong() { // Negative Outcome: Length too much
        AnimalDTO animalDTO = setupAnimalDTO();
        animalDTO.setLength(Double.parseDouble(Constants.MAX_ELDER_LENGTH.getValue()) + 1);
        String body = objectMapper.writeValueAsString(animalDTO);
        MvcResult result = getStatusResult(body, "BAD_REQUEST");
        AnimalError animalError = objectMapper.readValue(result.getResponse().getContentAsString(), AnimalError.class);
        assertThat(animalError,hasProperty("code",is(AnimalErrorCode.CRL_E0x16_2)));
        assertThat(animalError,hasProperty("message",is(AnimalErrorCode.CRL_E0x16_2.getMessage())));
    }

}
