package co.edu.icesi.calizoowebapp.integration;

import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(InitialDataConfig.class)
public class AfricanLionGetLionsIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @SneakyThrows
    public void getLionsSuccessfully(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/african-lion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<AfricanLionDTO> expectedAfricanLionList = basicAfricanLionList();
        List<AfricanLionDTO> resultAfricanLionList = objectMapper.readerForListOf(AfricanLionDTO.class).readValue(result.getResponse().getContentAsString());

        IntStream.range(0, expectedAfricanLionList.size()).forEach(index -> {
            AfricanLionDTO africanLionResult =  resultAfricanLionList.get(index);
            AfricanLionDTO africanLionExpected = expectedAfricanLionList.get(index);

            assertThat(africanLionResult, hasProperty("name", is(africanLionExpected.getName())));
            assertThat(africanLionResult, hasProperty("sex", is(africanLionExpected.getSex())));
            assertThat(africanLionResult, hasProperty("weight", is(africanLionExpected.getWeight())));
            assertThat(africanLionResult, hasProperty("height", is(africanLionExpected.getHeight())));
            assertThat(africanLionResult, hasProperty("age", is(africanLionExpected.getAge())));
            assertThat(africanLionResult, hasProperty("arrivedZooDate", is(africanLionExpected.getArrivedZooDate())));
            assertThat(africanLionResult, hasProperty("fatherId", is(africanLionExpected.getFatherId())));
            assertThat(africanLionResult, hasProperty("motherId", is(africanLionExpected.getMotherId())));

        });

    }

    @SneakyThrows
    private List<AfricanLionDTO> basicAfricanLionList(){
        String stringExpectedAfricanLionDTOList = parseResourceToString("getLions.json");
        return objectMapper.readerForListOf(AfricanLionDTO.class).readValue(stringExpectedAfricanLionDTOList);

    }


    @SneakyThrows
    private String parseResourceToString(String classPath){
        Resource resource = new ClassPathResource(classPath);
        try(Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)){
            return FileCopyUtils.copyToString(reader);
        }
    }
}
