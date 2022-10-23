package co.edu.icesi.spring_zoo_cusumbo.integration;


import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.error.ErrorCode;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.datasource.url=jdbc:h2:mem:testdb"}
        )
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //For resetting the database before each test
public class CreateCusumboIntegrationTest {

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
    public void createCusumboSuccesfully(){
        String body = parseResourceToString("createCusumbo.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();

        CusumboDTO userResult = objectMapper.readValue(result.getResponse().getContentAsString(),CusumboDTO.class);
        assertThat(userResult, hasProperty("name",is("Son")));
    }

    @SneakyThrows
    @Test
    public void createCusumboRepeatedName(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        String body = objectMapper.writeValueAsString(baseCusumbo);

        mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_01B.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_01B)));
    }
    @SneakyThrows
    @Test
    public void createCusumboInvalidSex(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setSex('Z');
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_06.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_06)));
    }

    @SneakyThrows
    @Test
    public void createCusumboInvalidNameLength(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setName(Strings.repeat("A",121));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_01A.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_01A)));
    }
    @SneakyThrows
    @Test
    public void createCusumboInvalidNameCharacters(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setName("InvalidName###");
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_01A.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_01A)));
    }

    @SneakyThrows
    @Test
    public void createCusumboDateIsAfterToday(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setArrivalDate(LocalDateTime.now().plusDays(1));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_02.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_02)));
    }

    @SneakyThrows
    @Test
    public void createCusumboWeightIsMoreThanMax(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setWeight(20F);
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_05.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_05)));
    }

    @SneakyThrows
    @Test
    public void createCusumboWeightIsLessOrEqualThanZero(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setWeight(-10F);
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_05.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_05)));
    }

    @SneakyThrows
    @Test
    public void createCusumboAgeIsMoreThanMax(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setAge(20);
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_03.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_03)));
    }

    @SneakyThrows
    @Test
    public void createCusumboAgeIsLessOrEqualThanZero(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setAge(-10);
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_03.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_03)));
    }

    @SneakyThrows
    @Test
    public void createCusumboHeightIsMoreThanMax(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setHeight(100);
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_04.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_04)));
    }

    @SneakyThrows
    @Test
    public void createCusumboHeightIsLessOrEqualThanZero(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setHeight(-10);
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_04.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_04)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsExistanceBothExist(){
        CusumboDTO baseCusumbo = createBaseCusumbo();

        baseCusumbo.setFatherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b85"));
        baseCusumbo.setMotherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b86"));

        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();

        CusumboDTO cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(),CusumboDTO.class);
        assertThat(cusumboResult, hasProperty("name",is("Son")));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsExistanceFatherDoesntExist(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setFatherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b89"));
        baseCusumbo.setMotherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b86"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07A.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07A)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsExistanceMotherDoesntExist(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setFatherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b85"));
        baseCusumbo.setMotherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b81"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07A.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07A)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsExistanceBothDonotExist(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setFatherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b74"));
        baseCusumbo.setMotherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b75"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07A.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07A)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsSexBothHaveSameSex(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setFatherId(UUID.fromString("7a558530-f06d-410f-868b-8884438d7b85"));
        baseCusumbo.setMotherId(UUID.fromString("1a558530-f06d-410f-868b-8884438d7b86"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07B.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07B)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsSexTranslocatedSex(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setFatherId(UUID.fromString("1a558530-f06d-410f-868b-8884438d7b85"));
        baseCusumbo.setMotherId(UUID.fromString("1a558530-f06d-410f-868b-8884438d7b86"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07C.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07C)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsSexOnlyFatherPresentAsFemale(){
        CusumboDTO baseCusumbo = createBaseCusumbo();
        baseCusumbo.setFatherId(UUID.fromString("1a558530-f06d-410f-868b-8884438d7b85"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07C.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07C)));
    }

    @SneakyThrows
    @Test
    public void createCusumboVerifyParentsSexOnlyMotherPresentAsMale(){
        CusumboDTO baseCusumbo = createBaseCusumbo();

        baseCusumbo.setMotherId(UUID.fromString("1a558530-f06d-410f-868b-8884438d7b86"));
        String body = objectMapper.writeValueAsString(baseCusumbo);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/animals/cusumbo")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();

        CusumboError cusumboResult = objectMapper.readValue(result.getResponse().getContentAsString(), CusumboError.class);
        assertThat(cusumboResult, hasProperty("message",is(ErrorCode.CODE_ATR_07C.getMessage())));
        assertThat(cusumboResult, hasProperty("errorCode",is(ErrorCode.CODE_ATR_07C)));
    }

    @SneakyThrows
    private CusumboDTO createBaseCusumbo(){
        String body = parseResourceToString("createCusumbo.json");
        return objectMapper.readValue(body,CusumboDTO.class);
    }


    @SneakyThrows
    private String parseResourceToString(String classPath){
        Resource resource = new ClassPathResource(classPath);

        try(Reader reader = new InputStreamReader(resource.getInputStream(),UTF_8)){
            return FileCopyUtils.copyToString(reader);
        }

    }

    /*
        Verify that the following is checked and the expected error is returned

        POST

        verify happy path OK

        verify name uniqueness OK

        verify own sex (valid char) OK

        verify parents
            -Existance in db
                -happy path (both parents present) OK
                -father missing OK
                -mother missing OK
                -both missing OK
            -Sex
                -happy oath OK
                -same sex OK
                -translocated sex OK
                -only father as female
                -only mother as male

        verify name length OK
        verify name characters OK
        verify date is before today OK
        verify weight OK
        verify age OK
        verify height OK


        GET ALL

        verify if there are 3 animals in the repository, 3 different animals are returned

        verify that empty list is returned if there are no animals on repository

        GET by name

        verify that a NOT FOUND error is returned if the repository is empty or if the animal was not found

        verify if an animal has no parents, only itself is returned

        verify if an animal has parents that the parent information is returned.

    */

}
