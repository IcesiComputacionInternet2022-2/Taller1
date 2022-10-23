package co.edu.icesi.apizoo.integration;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
import static liquibase.util.ObjectUtil.hasProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class AnimalIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }
/*
    @SneakyThrows
    @Test
    void createAnimal(){
        String body = parseResourceToString("createAnimal.json");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest()).andReturn();

        AnimalDTO animalDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AnimalDTO.class);

        assertThat(animalDTO, hasProperty("name", is("Juan")));
    }

    @SneakyThrows
    private String parseResourceToString(String classpath){
        Resource resource = new ClassPathResource(classpath);
        try(Reader reader = new InputStreamReader(resource.getInputStream())){
            return FileCopyUtils.copyToString(reader);
        }

    }*/
}
