package com.co.edu.icesi.zooWeb.integration;
import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanError;
import com.co.edu.icesi.zooWeb.integration.config.DataConfig;
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
import java.util.UUID;

import static com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
@Import({DataConfig.class})
@ActiveProfiles("test")
public class UpdateSwanTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String SWAN_NAME = "Carla";
    private static final String FATHER_UUID = "f06f879d-e262-4d10-a63d-a411e0c1f2e1";
    private static final String MOTHER_UUID = "e1eb87b8-cdb1-4d3a-8464-9303f1951a8e";


    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void updateSwan(){
        String body = parseResourceToString("updateSwan.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooweb/"+SWAN_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                        .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        BlackSwanDTO blackSwanDTO= objectMapper.readValue(result.getResponse().getContentAsString(),BlackSwanDTO.class);

        assertThat(blackSwanDTO,hasProperty("age",is(4)));
        assertThat(blackSwanDTO,hasProperty("weight",is(5.0)));
        assertThat(blackSwanDTO,hasProperty("height",is(110.0)));
    }

    @Test
    @SneakyThrows
    public void testMissing(){
        String body = parseResourceToString("updateSwan.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooweb/MISSING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_01)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_01.getMessage())));
    }


    @Test
    @SneakyThrows
    public void testFatherIsNotFemale(){
        BlackSwanDTO swanSource = swanSource();
        swanSource.setFatherId(UUID.fromString(MOTHER_UUID));
        String body = objectMapper.writeValueAsString(swanSource);
        MvcResult result = getBadRequestResult(body);

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_12)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_12.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testMotherIsNotMale(){
        BlackSwanDTO swanSource = swanSource();
        swanSource.setMotherId(UUID.fromString(FATHER_UUID));
        String body = objectMapper.writeValueAsString(swanSource);
        MvcResult result = getBadRequestResult(body);

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_12)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_12.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testSex(){
        BlackSwanDTO swanSource = swanSource();
        swanSource.setSex('M');
        String body = objectMapper.writeValueAsString(swanSource);
        MvcResult result = getBadRequestResult(body);

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_13)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_13.getMessage())));
    }

    @SneakyThrows
    private MvcResult getBadRequestResult(String body){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooweb/"+SWAN_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();
        return result;
    }

    @SneakyThrows
    private String parseResourceToString(String classpath) {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    @SneakyThrows
    private BlackSwanDTO swanSource(){
        String body = parseResourceToString("updateSwan.json");
        return objectMapper.readValue(body, BlackSwanDTO.class);
    }
}
