package icesi.edu.co.zoodemo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import icesi.edu.co.zoodemo.dto.SuricatoDTO;
import icesi.edu.co.zoodemo.dto.SuricatoParentsIdDTO;
import icesi.edu.co.zoodemo.error.exception.SuricatoError;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.time.LocalDateTime;
import java.util.UUID;

import static icesi.edu.co.zoodemo.constant.SuricatoErrorsCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
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
        assertThat(suricatoDTO, hasProperty("name", is("SuricatoSebastian")));

    }
    @SneakyThrows
    private String parseResourceToString(String classPath){
        Resource resource = new ClassPathResource(classPath);
        try(Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)){
            return FileCopyUtils.copyToString(reader);
        }
    }

    @Test
    @SneakyThrows
    public void invalidSizeNameTest(){
        SuricatoParentsIdDTO suricatoParentsIdDTO = createSuricatoExample();
        String name = "Sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
        suricatoParentsIdDTO.setName(name);
        String body = objectMapper.writeValueAsString(suricatoParentsIdDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/suricato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        SuricatoError ErrorLaunch = objectMapper.readValue(result.getResponse().getContentAsString(),SuricatoError.class);
        assertThat(ErrorLaunch,hasProperty("message",is(CODE_01.getMessage())));
    }
    private SuricatoParentsIdDTO createSuricatoExample(){
        SuricatoParentsIdDTO suricatoParentsIdDTO = new SuricatoParentsIdDTO();
        suricatoParentsIdDTO.setName("SuricataClaudia");
        suricatoParentsIdDTO.setArriveDate(LocalDateTime.now());
        suricatoParentsIdDTO.setFatherId(null);
        suricatoParentsIdDTO.setMotherId(null);
        suricatoParentsIdDTO.setAge(5);
        suricatoParentsIdDTO.setHeight(28);
        suricatoParentsIdDTO.setWeight(410);
        suricatoParentsIdDTO.setId(UUID.fromString("14d96594-9250-4a59-8423-2d2f461d673d"));
        suricatoParentsIdDTO.setGender("F");
        return suricatoParentsIdDTO;
    }

    @Test
    @SneakyThrows
    public void validateDateTest(){
        SuricatoParentsIdDTO suricatoParentsIdDTO = createSuricatoExample();
        suricatoParentsIdDTO.setArriveDate(LocalDateTime.of(2025
                , 5, 9, 8, 6, 56));;
        String body = objectMapper.writeValueAsString(suricatoParentsIdDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/suricato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        SuricatoError ErrorLaunch = objectMapper.readValue(result.getResponse().getContentAsString(), SuricatoError.class);
        assertThat(ErrorLaunch,hasProperty("code",is(CODE_06)));
    }
    @Test
    @SneakyThrows
    public void validateCharactersNameTest(){
        SuricatoParentsIdDTO suricatoParentsIdDTO = createSuricatoExample();
        suricatoParentsIdDTO.setName("SuricatoSebastian1");
        String body = objectMapper.writeValueAsString(suricatoParentsIdDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/suricato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        SuricatoError suricatoError = objectMapper.readValue(result.getResponse().getContentAsString(), SuricatoError.class);
        assertThat(suricatoError,hasProperty("code",is(CODE_05)));
    }
    @Test
    @SneakyThrows
    public void verifyWeightRange(){
        SuricatoParentsIdDTO suricatoParentsIdDTO = createSuricatoExample();
        suricatoParentsIdDTO.setHeight(5555);
        String body = objectMapper.writeValueAsString(suricatoParentsIdDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/suricato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        SuricatoError suricatoError = objectMapper.readValue(result.getResponse().getContentAsString(), SuricatoError.class);
        assertThat(suricatoError,hasProperty("code",is(CODE_08)));
    }

}

