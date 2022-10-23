package co.edu.icesi.calizoowebapp.integration;

import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionError;
import co.edu.icesi.calizoowebapp.integration.config.InitialDataConfig;
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
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(InitialDataConfig.class)
public class AfricanLionCreateLionIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @SneakyThrows
    public void createLionSuccessfully(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        AfricanLionDTO africanLionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionDTO.class);

        assertThat(africanLionResult, hasProperty("name", is("lionchild")));
        assertThat(africanLionResult, hasProperty("sex", is(AnimalSex.MALE)));
        assertThat(africanLionResult, hasProperty("weight", is(200.0)));
        assertThat(africanLionResult, hasProperty("height", is(105.0)));
        assertThat(africanLionResult, hasProperty("age", is(0)));
        assertThat(africanLionResult, hasProperty("arrivedZooDate", is(LocalDateTime.parse("2021-12-10 08:10:59",  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))));
        assertThat(africanLionResult, hasProperty("fatherId", is(UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132"))));
        assertThat(africanLionResult, hasProperty("motherId", is(UUID.fromString("cbe5ea52-0edb-4d2e-a883-1488f1520b20"))));

    }


    @Test
    @SneakyThrows
    public void createLionRepeated(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lionfather");
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_02)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_02.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionParentsSameSex(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setMotherId(UUID.fromString("729734fc-014f-46d5-9e2e-958809363c6a"));
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_03)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_03.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionHeightOutOfStandardsMaleHeight(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setHeight(160.0);
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_04)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_04.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionHeightOutOfStandardsFemaleHeight(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setWeight(160.0);
        baseAfricanLion.setHeight(120.0);
        baseAfricanLion.setSex(AnimalSex.FEMALE);
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_04)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_04.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionHeightOutOfStandardsMaleWeight(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setWeight(260.0);
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_06)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_06.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionHeightOutOfStandardsFemaleWeight(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setWeight(200.0);
        baseAfricanLion.setSex(AnimalSex.FEMALE);
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_06)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_06.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionHeightOutOfStandardsMaleAge(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setAge(20);
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_05)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_05.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionHeightOutOfStandardsFemaleAge(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setWeight(160.0);
        baseAfricanLion.setHeight(100.0);
        baseAfricanLion.setAge(16);
        baseAfricanLion.setSex(AnimalSex.FEMALE);
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_05)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_05.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionParentsNotSaved(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setFatherId(UUID.fromString("10a91b05-4621-4a79-9c94-372fa108d110"));
        baseAfricanLion.setMotherId(UUID.fromString("80787b9d-5485-3102-005c-a658e018f71c"));
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_10)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_10.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionInvalidArrivedDate(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid");
        baseAfricanLion.setArrivedZooDate(LocalDateTime.parse("2023-12-10T08:10:59"));
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_09)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_09.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionNameLongerThan120Chars(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalidddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_07)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_07.getMessage())));
    }

    @Test
    @SneakyThrows
    public void createLionNameSpecialChars(){
        AfricanLionDTO baseAfricanLion = baseAfricanLion();
        baseAfricanLion.setName("lioninvalid1@");
        String body = objectMapper.writeValueAsString(baseAfricanLion);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_08)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_08.getMessage())));
    }


    @SneakyThrows
    private AfricanLionDTO baseAfricanLion(){
        String body = parseResourceToString("createAfricanLion.json");
        return objectMapper.readValue(body, AfricanLionDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString(String classPath){
        Resource resource = new ClassPathResource(classPath);
        try(Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)){
            return FileCopyUtils.copyToString(reader);
        }
    }

}
