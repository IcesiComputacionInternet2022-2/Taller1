package com.co.edu.icesi.zooWeb.integration;


import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanError;
import com.co.edu.icesi.zooWeb.integration.config.DataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDateTime;

import static com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
@Import({DataConfig.class})
@ActiveProfiles("test")
public class CreateSwanTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @SneakyThrows
    public void createSwan(){
        String body = parseResourceToString("createSwan.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooweb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        BlackSwanDTO blackSwanDTO= objectMapper.readValue(result.getResponse().getContentAsString(),BlackSwanDTO.class);
        assertThat(blackSwanDTO,hasProperty("name",is("Perry")));
    }

    @Test
    @SneakyThrows
    public void createSwanRepeated(){
        String body = parseResourceToString("createSwan.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooweb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isConflict())
                .andReturn();

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_11)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_11.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCorrectName(){
        BlackSwanDTO blackSwanSource = blackSwanSource();
        blackSwanSource.setName("1234");
        String body = objectMapper.writeValueAsString(blackSwanSource);
        MvcResult result = getBadRequestResult(body);
        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_02)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_02.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testCorrectName2(){
        BlackSwanDTO blackSwanDTO = blackSwanSource();
        String name = StringUtils.repeat("a", 121);
        blackSwanDTO.setName(name);
        String body = objectMapper.writeValueAsString(blackSwanDTO);
        MvcResult result = getBadRequestResult(body);

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_07)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_07.getMessage())));
    }

    @SneakyThrows
    private MvcResult getBadRequestResult(String body){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/zooweb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();
        return result;
    }

    @Test
    @SneakyThrows
    public void testDate(){
        BlackSwanDTO blackSwanDTO = blackSwanSource();
        blackSwanDTO.setArrivedZooDate(LocalDateTime.MAX);
        String body = objectMapper.writeValueAsString(blackSwanDTO);
        MvcResult result = getBadRequestResult(body);
        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("message",is(CODE_13.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testHeight(){
        BlackSwanDTO blackSwanDTO = blackSwanSource();
        blackSwanDTO.setHeight(141);
        String body = objectMapper.writeValueAsString(blackSwanDTO);
        MvcResult result = getBadRequestResult(body);
        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_03)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_03.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testWeight(){
        BlackSwanDTO blackSwanDTO = blackSwanSource();
        blackSwanDTO.setWeight(10);
        String body = objectMapper.writeValueAsString(blackSwanDTO);
        MvcResult result = getBadRequestResult(body);
        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_06)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_06.getMessage())));
    }

    @Test
    @SneakyThrows
    public void testAge(){
        BlackSwanDTO blackSwanDTO = blackSwanSource();
        blackSwanDTO.setAge(25);
        String body = objectMapper.writeValueAsString(blackSwanDTO);
        MvcResult result = getBadRequestResult(body);
        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_04)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_04.getMessage())));
    }

    @SneakyThrows
    private String parseResourceToString(String classpath) {
        Resource resource = new ClassPathResource(classpath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    @SneakyThrows
    private BlackSwanDTO blackSwanSource(){
        String body = parseResourceToString("createSwan.json");
        return objectMapper.readValue(body, BlackSwanDTO.class);
    }

}

