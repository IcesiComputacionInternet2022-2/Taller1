package co.edu.icesi.zoo.integration;

import co.edu.icesi.zoo.dto.TatabroDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooGetTatabrosIntegrationTest {

    private static final UUID TATABRO_1_UUID = UUID.fromString("b55d9d91-2d6f-48f6-9442-8f654a0aba47");
    private static final UUID TATABRO_2_UUID = UUID.fromString("5631cbd3-cf53-415f-bd06-4e995ee3c322");
    private static final UUID TATABRO_3_UUID = UUID.fromString("2f7908f1-c6c5-4fd9-bca9-07332d2c60e6");

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @SneakyThrows
    public void getTatabrosSuccessfully() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tatabros/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        TatabroDTO[] tatabroResult = objectMapper.readValue(result.getResponse().getContentAsString(), TatabroDTO[].class);
        assertThat(tatabroResult[0], hasProperty("id", is(TATABRO_1_UUID)));
        assertThat(tatabroResult[0], hasProperty("name", is("Tommy")));
        assertThat(tatabroResult[0], hasProperty("sex", is("M")));
        assertThat(tatabroResult[0], hasProperty("weight", is(24.0)));
        assertThat(tatabroResult[0], hasProperty("age", is(18)));
        assertThat(tatabroResult[0], hasProperty("height", is(53.0)));
        assertThat(tatabroResult[0], hasProperty("arrivalDate", is(LocalDateTime.parse("2018-12-31T10:10:10"))));
        assertThat(tatabroResult[0], hasProperty("fatherID", nullValue()));
        assertThat(tatabroResult[0], hasProperty("motherID", nullValue()));
        assertThat(tatabroResult[1], hasProperty("id", is(TATABRO_2_UUID)));
        assertThat(tatabroResult[1], hasProperty("name", is("Dory")));
        assertThat(tatabroResult[1], hasProperty("sex", is("F")));
        assertThat(tatabroResult[1], hasProperty("weight", is(25.0)));
        assertThat(tatabroResult[1], hasProperty("age", is(17)));
        assertThat(tatabroResult[1], hasProperty("height", is(55.0)));
        assertThat(tatabroResult[1], hasProperty("arrivalDate", is(LocalDateTime.parse("2019-12-19T11:18:10"))));
        assertThat(tatabroResult[1], hasProperty("fatherID", nullValue()));
        assertThat(tatabroResult[1], hasProperty("motherID", nullValue()));
        assertThat(tatabroResult[2], hasProperty("id", is(TATABRO_3_UUID)));
        assertThat(tatabroResult[2], hasProperty("name", is("Piggy")));
        assertThat(tatabroResult[2], hasProperty("sex", is("M")));
        assertThat(tatabroResult[2], hasProperty("weight", is(20.0)));
        assertThat(tatabroResult[2], hasProperty("age", is(15)));
        assertThat(tatabroResult[2], hasProperty("height", is(50.0)));
        assertThat(tatabroResult[2], hasProperty("arrivalDate", is(LocalDateTime.parse("2021-12-13T11:13:19"))));
        assertThat(tatabroResult[2], hasProperty("fatherID", is(TATABRO_1_UUID)));
        assertThat(tatabroResult[2], hasProperty("motherID", is(TATABRO_2_UUID)));
    }
}