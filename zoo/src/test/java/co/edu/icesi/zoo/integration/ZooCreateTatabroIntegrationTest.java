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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooCreateTatabroIntegrationTest {

    private static final String JSON_FILE = "createTatabro.json";
    private static final UUID TATABRO_UUID_NON_EXISTENT = UUID.fromString("35a81e7d-342b-48e2-89e3-cccbb6e09f25");
    private static final UUID TATABRO_FATHER_UUID_EXISTENT = UUID.fromString("b55d9d91-2d6f-48f6-9442-8f654a0aba47");
    private static final UUID TATABRO_MOTHER_UUID_EXISTENT = UUID.fromString("5631cbd3-cf53-415f-bd06-4e995ee3c322");

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
    public void createTatabroWithoutParentsSuccessfully() {
        TatabroDTO baseTatabro = baseTatabro();
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();
        TatabroDTO tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroDTO.class);
        assertThat(tatabroResult, hasProperty("name", is("Sammy Pig")));
        assertThat(tatabroResult, hasProperty("sex", is("M")));
        assertThat(tatabroResult, hasProperty("weight", is(20.0)));
        assertThat(tatabroResult, hasProperty("age", is(15)));
        assertThat(tatabroResult, hasProperty("height", is(50.0)));
        assertThat(tatabroResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2021-01-16T10:30:25"))));
        assertThat(tatabroResult, hasProperty("fatherID", nullValue()));
        assertThat(tatabroResult, hasProperty("motherID", nullValue()));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithParentsSuccessfully() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setName("Poppy");
        baseTatabro.setSex("F");
        baseTatabro.setFatherID(TATABRO_FATHER_UUID_EXISTENT);
        baseTatabro.setMotherID(TATABRO_MOTHER_UUID_EXISTENT);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();
        TatabroDTO tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroDTO.class);
        assertThat(tatabroResult, hasProperty("name", is("Poppy")));
        assertThat(tatabroResult, hasProperty("sex", is("F")));
        assertThat(tatabroResult, hasProperty("weight", is(20.0)));
        assertThat(tatabroResult, hasProperty("age", is(15)));
        assertThat(tatabroResult, hasProperty("height", is(50.0)));
        assertThat(tatabroResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2021-01-16T10:30:25"))));
        assertThat(tatabroResult, hasProperty("fatherID", is(TATABRO_FATHER_UUID_EXISTENT)));
        assertThat(tatabroResult, hasProperty("motherID", is(TATABRO_MOTHER_UUID_EXISTENT)));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithRepeatedName() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setName("Tommy");
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_03)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's name already exists.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithFatherNonExistent() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setFatherID(TATABRO_UUID_NON_EXISTENT);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_01)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro ID doesn't exist.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithMotherNonExistent() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setMotherID(TATABRO_UUID_NON_EXISTENT);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_01)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro ID doesn't exist.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithParentsSameSexMale() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setFatherID(TATABRO_FATHER_UUID_EXISTENT);
        baseTatabro.setMotherID(TATABRO_FATHER_UUID_EXISTENT);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_04)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's father must be a male and tatabro's mother must be a female.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithParentsSameSexFemale() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setFatherID(TATABRO_MOTHER_UUID_EXISTENT);
        baseTatabro.setMotherID(TATABRO_MOTHER_UUID_EXISTENT);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.CODE_04)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's father must be a male and tatabro's mother must be a female.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithNameNull() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setName(null);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Attribute 'name' cannot be null.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithNumbersInName() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setName("Tommy1");
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Names cannot exceed 120 digits and can only contain letters and spaces.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithSpecialCharactersInName() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setName("T@mmy");
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Names cannot exceed 120 digits and can only contain letters and spaces.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithSexNull() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setSex(null);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Attribute 'sex' cannot be null.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithIncoherentSex() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setSex("Z");
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's sex doesn't match with M/m (Male) or F/f (Female).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithWeightNull() {
        TatabroDTO baseTatabro = baseTatabro();
        String body = objectMapper.writeValueAsString(baseTatabro).replace("20.0", "null");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's weight doesn't correspond to tatabro's weight average (between 20 kg and 40 kg).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithWeightLessThan20kg() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setWeight(19.99);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's weight doesn't correspond to tatabro's weight average (between 20 kg and 40 kg).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithWeightLessThan40kg() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setWeight(40.01);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's weight doesn't correspond to tatabro's weight average (between 20 kg and 40 kg).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithAgeNull() {
        TatabroDTO baseTatabro = baseTatabro();
        String body = objectMapper.writeValueAsString(baseTatabro).replace("15", "null");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's age doesn't correspond to tatabro's age average (between 15 and 20 years old).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithAgeLessThan15YearsOld() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setAge(14);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's age doesn't correspond to tatabro's age average (between 15 and 20 years old).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithAgeMoreThan20YearsOld() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setAge(21);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's age doesn't correspond to tatabro's age average (between 15 and 20 years old).")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithHeightNull() {
        TatabroDTO baseTatabro = baseTatabro();
        String body = objectMapper.writeValueAsString(baseTatabro).replace("50.0", "null");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's height doesn't correspond to tatabro's height average (between 50 cm and 90 cm)")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithHeightLessThan50cm() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setHeight(49.99);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's height doesn't correspond to tatabro's height average (between 50 cm and 90 cm)")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithHeightLessThan90cm() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setHeight(90.01);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Tatabro's height doesn't correspond to tatabro's height average (between 50 cm and 90 cm)")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithArrivalDateNull() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setArrivalDate(null);
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("Attribute 'arrivalDate' cannot be null.")));
    }

    @Test
    @SneakyThrows
    public void createTatabroWithArrivalDateAfterCurrentDate() {
        TatabroDTO baseTatabro = baseTatabro();
        baseTatabro.setArrivalDate(LocalDateTime.now().plusDays(1));
        String body = objectMapper.writeValueAsString(baseTatabro);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tatabros").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        TatabroError tatabroError = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroError.class);
        assertThat(tatabroError, hasProperty("code", is(TatabroErrorCode.UNIVERSAL_ANNOTATION_CODE)));
        assertThat(tatabroError, hasProperty("message", is("The arrival date cannot be after the current date and time.")));
    }

    @SneakyThrows
    private TatabroDTO baseTatabro() {
        String body = parseResourceToString();
        return objectMapper.readValue(body, TatabroDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString() {
        Resource resource = new ClassPathResource(JSON_FILE);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}