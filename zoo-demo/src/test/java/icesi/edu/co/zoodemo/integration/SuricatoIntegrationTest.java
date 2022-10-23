package icesi.edu.co.zoodemo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import icesi.edu.co.zoodemo.dto.SuricatoDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)

public class SuricatoIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();


    }
    @SneakyThrows
    @Test
    public void createSuricato(){
        String body = parseResourceToString("create_suricato.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/suricato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isOk())
                .andReturn();
        SuricatoDTO suricatoDTO = objectMapper.readValue(result.getResponse().getContentAsString(),SuricatoDTO.class);
        assertThat(suricatoDTO, hasProperty("name", is("Suricata")));

    }
    @SneakyThrows
    private String parseResourceToString(String classPath){
        Resource resource = new ClassPathResource(classPath);
        try(Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)){
            return FileCopyUtils.copyToString(reader);
        }
    }

}

