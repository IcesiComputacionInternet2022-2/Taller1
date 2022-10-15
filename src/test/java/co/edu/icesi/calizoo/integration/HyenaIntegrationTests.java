package co.edu.icesi.calizoo.integration;


import co.edu.icesi.calizoo.dto.HyenaDTO;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HyenaIntegrationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    @BeforeEach
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @Test
    @SneakyThrows
    public void createUserSuccessfully() {
        String body = parseResourceToString("createHyena.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/hyenas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)).andExpect(status().isOk())
                    .andReturn();

        HyenaDTO userResult = objectMapper.readValue(result.getResponse().getContentAsString(),HyenaDTO.class);

        assertThat(userResult,hasProperty("name",is("Alex")));

    }
    @SneakyThrows
    private String parseResourceToString (String classPath) {
        Resource resource = new ClassPathResource(classPath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(),UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
