package co.icesi.edu.animals.integration;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import co.icesi.edu.animals.dto.CaripiareDTO;
import co.icesi.edu.animals.error.exception.CaripiareError;
import co.icesi.edu.animals.integration.config.InitialDataConfig;
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
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({InitialDataConfig.class})
public class AnimalsCreateCaripiareIntegrationTest {

    private static final UUID FATHER_CARIPIARE_UUID = UUID.fromString("f3bf9478-4be3-4f4b-a4b1-b2f84495a1eb");
    private static final UUID MOTHER_CARIPIARE_UUID = UUID.fromString("c51e2f97-aaae-449b-87ae-1a38e4414132");
    private static final UUID FATHER_TWO_CARIPIARE_UUID = UUID.fromString("a5756676-c059-4881-a0ea-3c8d2907e3b8");
    private static final UUID MOTHER_TWO_CARIPIARE_UUID = UUID.fromString("fcb02990-c111-445a-8c3f-37ef9b162c3a");

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

    @Test
    @SneakyThrows
    public void testCreateCaripiare() {
        CaripiareDTO baseCaripiareDTO = getBaseCaripiareDTO();
        String body = objectMapper.writeValueAsString(baseCaripiareDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/caripiares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();
        CaripiareDTO caripiareDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareDTO.class);
        assertThat(caripiareDTO, hasProperty("name", is(baseCaripiareDTO.getName())));
        assertThat(caripiareDTO, hasProperty("name", is(baseCaripiareDTO.getName())));
        assertThat(caripiareDTO, hasProperty("gender", is(baseCaripiareDTO.getGender())));
        assertThat(caripiareDTO, hasProperty("weight", is(baseCaripiareDTO.getWeight())));
        assertThat(caripiareDTO, hasProperty("age", is(baseCaripiareDTO.getAge())));
        assertThat(caripiareDTO, hasProperty("height", is(baseCaripiareDTO.getHeight())));
        assertThat(caripiareDTO.getArrivalDate(), is(baseCaripiareDTO.getArrivalDate()));
    }

    @Test
    @SneakyThrows
    public void testCreateCaripiareAndParents() {
        CaripiareDTO baseCaripiareDTO = getBaseCaripiareDTO();
        baseCaripiareDTO.setFatherId(FATHER_CARIPIARE_UUID);
        baseCaripiareDTO.setMotherId(MOTHER_CARIPIARE_UUID);
        //Renaming baseCaripiareDTO to avoid duplicate name exception
        baseCaripiareDTO.setName("caripiare with parents");
        String body = objectMapper.writeValueAsString(baseCaripiareDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/caripiares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();
        CaripiareDTO caripiareDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareDTO.class);
        assertThat(caripiareDTO, hasProperty("name", is(baseCaripiareDTO.getName())));
        assertThat(caripiareDTO, hasProperty("gender", is(baseCaripiareDTO.getGender())));
        assertThat(caripiareDTO, hasProperty("weight", is(baseCaripiareDTO.getWeight())));
        assertThat(caripiareDTO, hasProperty("age", is(baseCaripiareDTO.getAge())));
        assertThat(caripiareDTO, hasProperty("height", is(baseCaripiareDTO.getHeight())));
        assertThat(caripiareDTO.getArrivalDate(), is(baseCaripiareDTO.getArrivalDate()));
        assertThat(caripiareDTO, hasProperty("fatherId", is(FATHER_CARIPIARE_UUID)));
        assertThat(caripiareDTO, hasProperty("motherId", is(MOTHER_CARIPIARE_UUID)));
    }

    @Test
    @SneakyThrows
    public void createCaripiareAndOneNullParent() {
        CaripiareDTO baseCaripiareDTO = getBaseCaripiareDTO();
        baseCaripiareDTO.setFatherId(FATHER_CARIPIARE_UUID);
        //Renaming baseCaripiareDTO to avoid duplicate name exception
        baseCaripiareDTO.setName("caripiare with one parent");
        String body = objectMapper.writeValueAsString(baseCaripiareDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/caripiares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isOk())
                .andReturn();

        CaripiareDTO caripiareDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareDTO.class);
        assertThat(caripiareDTO, hasProperty("name", is(baseCaripiareDTO.getName())));
        assertThat(caripiareDTO, hasProperty("gender", is(baseCaripiareDTO.getGender())));
        assertThat(caripiareDTO, hasProperty("weight", is(baseCaripiareDTO.getWeight())));
        assertThat(caripiareDTO, hasProperty("age", is(baseCaripiareDTO.getAge())));
        assertThat(caripiareDTO, hasProperty("height", is(baseCaripiareDTO.getHeight())));
        assertThat(caripiareDTO.getArrivalDate(), is(baseCaripiareDTO.getArrivalDate()));
        assertThat(caripiareDTO, hasProperty("fatherId", is(FATHER_CARIPIARE_UUID)));
    }

    @Test
    @SneakyThrows
    public void createCaripiareDuplicatedName() {
        CaripiareDTO baseCaripiareDTO = getBaseCaripiareDTO();
        //Name already existing in database
        baseCaripiareDTO.setName("father");
        String body = objectMapper.writeValueAsString(baseCaripiareDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/caripiares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();
        CaripiareError caripiareError = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareError.class);
        assertThat(caripiareError, hasProperty("caripiareErrorCode", is(CaripiareErrorCode.CODE_01)));
    }

    @Test
    @SneakyThrows
    public void createCaripiareAndParentsFemaleGenderValidation() {
        CaripiareDTO baseCaripiareDTO = getBaseCaripiareDTO();
        baseCaripiareDTO.setFatherId(MOTHER_TWO_CARIPIARE_UUID);
        baseCaripiareDTO.setMotherId(MOTHER_CARIPIARE_UUID);
        //Renaming baseCaripiareDTO to avoid duplicate name exception
        baseCaripiareDTO.setName("caripiare with two mothers");
        String body = objectMapper.writeValueAsString(baseCaripiareDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/caripiares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        CaripiareError caripiareError = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareError.class);
        assertThat(caripiareError, hasProperty("caripiareErrorCode", is(CaripiareErrorCode.CODE_03)));
    }

    @Test
    @SneakyThrows
    public void createCaripiareAndParentsMaleGenderValidation() {
        CaripiareDTO baseCaripiareDTO = getBaseCaripiareDTO();
        baseCaripiareDTO.setFatherId(FATHER_CARIPIARE_UUID);
        baseCaripiareDTO.setMotherId(FATHER_TWO_CARIPIARE_UUID);
        //Renaming baseCaripiareDTO to avoid duplicate name exception
        baseCaripiareDTO.setName("caripiare with two fathers");
        String body = objectMapper.writeValueAsString(baseCaripiareDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/caripiares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)).andExpect(status().isBadRequest())
                .andReturn();

        CaripiareError caripiareError = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CaripiareError.class);
        assertThat(caripiareError, hasProperty("caripiareErrorCode", is(CaripiareErrorCode.CODE_03)));
    }

    @SneakyThrows
    private CaripiareDTO getBaseCaripiareDTO(){
        String body = parseResourceToString();
        return objectMapper.readValue(body, CaripiareDTO.class);
    }

    @SneakyThrows
    private String parseResourceToString() {
        Resource resource = new ClassPathResource("createCaripiare.json");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
