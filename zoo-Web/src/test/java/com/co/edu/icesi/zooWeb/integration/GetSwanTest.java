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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode.CODE_01;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "spring.datasource.url=jdbc:h2:mem:testdb" })
@Import({DataConfig.class})
@ActiveProfiles("test")
public class GetSwanTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SWAN_NAME = "Patricia";

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    @SneakyThrows
    public void getSwan() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/zooweb/" + SWAN_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        BlackSwanDTO[] blackSwanDTOS = blackSwanDTOS(response);


        assertThat(blackSwanDTOS[0], hasProperty("name", is("Karen")));
        assertThat(blackSwanDTOS[1], hasProperty("name", is("Fernando")));
        assertThat(blackSwanDTOS[2], hasProperty("name", is("Carla")));
    }

    @Test
    @SneakyThrows
    public void getSwans() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/zooweb")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        BlackSwanDTO[] blackSwanDTOS = blackSwanDTOS(response);
        assertThat(blackSwanDTOS[0], hasProperty("name", is("Fernando")));
        assertThat(blackSwanDTOS[1], hasProperty("name", is("Carla")));
        assertThat(blackSwanDTOS[2], hasProperty("name", is("Karen")));
    }
    @SneakyThrows
    private BlackSwanDTO[] blackSwanDTOS(String response){
        String[] blackSwans = response.substring(1,response.length()-1).split("},");
        BlackSwanDTO[] blackSwanDTOS = new BlackSwanDTO[blackSwans.length];
        for(int i =0;i<blackSwans.length;i++){
            blackSwans[i] = blackSwans[i]+"}";
            blackSwanDTOS[i]= objectMapper.readValue(blackSwans[i], BlackSwanDTO.class);
        }
        return blackSwanDTOS;
    }

    @Test
    @SneakyThrows
    public void testMissing() {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/zooweb/MISSING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        BlackSwanError blackSwanError = objectMapper.readValue(result.getResponse().getContentAsString(), BlackSwanError.class);
        assertThat(blackSwanError,hasProperty("code",is(CODE_01)));
        assertThat(blackSwanError,hasProperty("message",is(CODE_01.getMessage())));
    }
}
