package com.edu.icesi.CaliZoo.integrationTest;

import com.edu.icesi.CaliZoo.dto.ToucanDTO;
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
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToucanIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    private void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }//End init

    @SneakyThrows
    @Test
    public void createToucanSuccessfully(){
        String body = parseResourceToString("createToucan.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/toucans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isOk()).andReturn();
        ToucanDTO userResult = objectMapper.readValue(result.getResponse().getContentAsString(), ToucanDTO.class);
        assertThat(userResult, hasProperty("name", is("Brian") ));
    }//End createToucanSuccessfully

    @SneakyThrows
    private String parseResourceToString(String classPath) {
        Resource resources = new ClassPathResource(classPath);
        try(Reader reader = new InputStreamReader(resources.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}//End ToucanIntegrationTest
