package co.edu.icesi.calizoowebapp.integration;

import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
import co.edu.icesi.calizoowebapp.dto.AfricanLionQueryResponseDTO;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(InitialDataConfig.class)
public class AfricanLionGetLionIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    private static final String AFRICAN_LION_NAME = "lionfather";

    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @SneakyThrows
    public void getLionSuccessfully(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/african-lion/" + AFRICAN_LION_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AfricanLionQueryResponseDTO resultQueryResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionQueryResponseDTO.class);
        AfricanLionDTO requestedLion = resultQueryResponse.getRequestedLion();
        AfricanLionDTO fatherLion = resultQueryResponse.getLionFather();
        AfricanLionDTO motherLion = resultQueryResponse.getLionMother();

        assertThat(requestedLion, hasProperty("name", is(AFRICAN_LION_NAME)));
        assertThat(requestedLion, hasProperty("sex", is(AnimalSex.MALE)));
        assertThat(requestedLion, hasProperty("weight", is(240.0)));
        assertThat(requestedLion, hasProperty("height", is(110.0)));
        assertThat(requestedLion, hasProperty("age", is(5)));
        assertThat(requestedLion, hasProperty("arrivedZooDate", is(LocalDateTime.parse("2020-09-10 08:10:59",  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))));
        assertThat(requestedLion, hasProperty("fatherId", nullValue()));
        assertThat(requestedLion, hasProperty("motherId", nullValue()));

        assertThat(fatherLion, nullValue());
        assertThat(motherLion, nullValue());
    }

    @Test
    @SneakyThrows
    public void getLionNotSaved(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/african-lion/" + "notsavedlion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_01)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_01.getMessage())));
    }

    @Test
    @SneakyThrows
    public void getLionNameSpecialChars(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/african-lion/" + "invalidlion1@")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_08)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_08.getMessage())));
    }

    @Test
    @SneakyThrows
    public void getLionNameLongerThan120Chars(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/african-lion/" + "lionChilddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        AfricanLionError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), AfricanLionError.class);

        assertThat(exceptionResult, hasProperty("code", is(AfricanLionErrorCode.CODE_07)));
        assertThat(exceptionResult, hasProperty("message", is(AfricanLionErrorCode.CODE_07.getMessage())));
    }


}
