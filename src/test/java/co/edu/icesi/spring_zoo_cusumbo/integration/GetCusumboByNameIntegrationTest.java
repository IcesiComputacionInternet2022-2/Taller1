package co.edu.icesi.spring_zoo_cusumbo.integration;


import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.error.ErrorCode;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.datasource.url=jdbc:h2:mem:testdb"}
)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //For resetting the database before each test

/*
Runs OK When:
Maven clean then install
Only this class is run

Fails When:
Run all test package, somehow the database does not reset

 */

public class GetCusumboByNameIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper.findAndRegisterModules();
    }

    @SneakyThrows
    @Test
    public void getByNameNotInRepo(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals/cusumbo/NotInRepoName")
                .contentType(MediaType.APPLICATION_JSON).content("")).andExpect(status().isNotFound()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_SEARCH_01.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_SEARCH_01)));
    }

    @SneakyThrows
    @Test
    @SuppressWarnings("rawtypes")
    public void getByNameInRepoWithNoParents(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals/cusumbo/Father")
                .contentType(MediaType.APPLICATION_JSON).content("")).andExpect(status().isOk()).andReturn();

        List cusumbosResult = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(1,cusumbosResult.size());
        assertEquals(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b85"), objectMapper.convertValue(cusumbosResult.get(0),CusumboDTO.class).getId());

    }

    @SneakyThrows
    @Test
    @SuppressWarnings("rawtypes")
    public void getByNameInRepoWithParents(){
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/animals/cusumbo/Test")
                .contentType(MediaType.APPLICATION_JSON).content("")).andExpect(status().isOk()).andReturn();

        List cusumbosResult = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(3,cusumbosResult.size());
        assertEquals(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b84"), objectMapper.convertValue(cusumbosResult.get(0),CusumboDTO.class).getId());
        assertEquals(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b85"), objectMapper.convertValue(cusumbosResult.get(1),CusumboDTO.class).getId());
        assertEquals(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b86"), objectMapper.convertValue(cusumbosResult.get(2),CusumboDTO.class).getId());
    }
}
