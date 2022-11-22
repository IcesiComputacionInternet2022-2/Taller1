package co.edu.icesi.zoo.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.icesi.zoo.constant.OstrichErrorCode;
import co.edu.icesi.zoo.dto.OstrichDTO;
import co.edu.icesi.zoo.error.exception.OstrichError;
import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class ZooCreateOstrichTests {
	
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
    @SneakyThrows
    public void createOstrichWithoutParents(){
        OstrichDTO baseOstrich = baseOstrich();
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();
        OstrichDTO ostrichResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichDTO.class);
        assertThat(ostrichResult, hasProperty("name", is("Medium Ostrich")));
        assertThat(ostrichResult, hasProperty("gender", is(1)));
        assertThat(ostrichResult, hasProperty("weight", is(65.5f)));
        assertThat(ostrichResult, hasProperty("age", is(35)));
        assertThat(ostrichResult, hasProperty("height", is(1.75f)));
        assertThat(ostrichResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-15T12:00:00"))));
        assertThat(ostrichResult, hasProperty("fatherId", nullValue()));
        assertThat(ostrichResult, hasProperty("motherId", nullValue()));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithFather(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Medium Ostrich With Father");
        baseOstrich.setFatherId(UUID.fromString("5afba3a1-96ba-46eb-b9c1-9ac690953fb4"));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();
        OstrichDTO ostrichResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichDTO.class);
        assertThat(ostrichResult, hasProperty("name", is("Medium Ostrich With Father")));
        assertThat(ostrichResult, hasProperty("gender", is(1)));
        assertThat(ostrichResult, hasProperty("weight", is(65.5f)));
        assertThat(ostrichResult, hasProperty("age", is(35)));
        assertThat(ostrichResult, hasProperty("height", is(1.75f)));
        assertThat(ostrichResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-15T12:00:00"))));
        assertThat(ostrichResult, hasProperty("fatherId", is(UUID.fromString("5afba3a1-96ba-46eb-b9c1-9ac690953fb4"))));
        assertThat(ostrichResult, hasProperty("motherId", nullValue()));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithMother(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Medium Ostrich With Mother");
        baseOstrich.setMotherId(UUID.fromString("59b99314-ed1a-4678-ab05-463b186c10c3"));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andReturn();
        OstrichDTO ostrichResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichDTO.class);
        assertThat(ostrichResult, hasProperty("name", is("Medium Ostrich With Mother")));
        assertThat(ostrichResult, hasProperty("gender", is(1)));
        assertThat(ostrichResult, hasProperty("weight", is(65.5f)));
        assertThat(ostrichResult, hasProperty("age", is(35)));
        assertThat(ostrichResult, hasProperty("height", is(1.75f)));
        assertThat(ostrichResult, hasProperty("arrivalDate", is(LocalDateTime.parse("2022-10-15T12:00:00"))));
        assertThat(ostrichResult, hasProperty("fatherId", nullValue()));
        assertThat(ostrichResult, hasProperty("motherId", is(UUID.fromString("59b99314-ed1a-4678-ab05-463b186c10c3"))));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithRepeatName(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Small Ostrich");
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_01.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_01.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidNameLength(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaliiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiid Ostriiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiich");
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_02.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_02.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidName(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid @strich");
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_03.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_03.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidArrivalDate(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setArrivalDate(LocalDateTime.now().plusDays(1));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_04.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_04.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidWeight(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setWeight(62.2f);
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_05.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_05.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidAge(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setAge(80);
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_06.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_06.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidHeight(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setHeight(1.50f);
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_07.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_07.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidGender(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setGender(2);
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_08.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_08.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithFatherNotFound(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setFatherId(UUID.fromString("59b99314-ed1a-4678-ab05-463b186c10c2"));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_09.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_09.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithMotherNotFound(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Invalid Ostrich");
        baseOstrich.setMotherId(UUID.fromString("5afba3a1-96ba-46eb-b9c1-9ac690953fb3"));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_10.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_10.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidFather(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Medium Ostrich With Invalid Father");
        baseOstrich.setFatherId(UUID.fromString("59b99314-ed1a-4678-ab05-463b186c10c3"));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_11.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_11.getMessage())));
    }
	
	@Test
    @SneakyThrows
    public void createOstrichWithInvalidMother(){
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Medium Ostrich With Invalid Mother");
        baseOstrich.setMotherId(UUID.fromString("5afba3a1-96ba-46eb-b9c1-9ac690953fb4"));
        String body = objectMapper.writeValueAsString(baseOstrich);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ostrich").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andReturn();
        OstrichError exceptionResult = objectMapper.readValue(result.getResponse().getContentAsString(), OstrichError.class);
        assertThat(exceptionResult, hasProperty("code", is(OstrichErrorCode.CODE_12.name())));
        assertThat(exceptionResult, hasProperty("message", is(OstrichErrorCode.CODE_12.getMessage())));
    }

	private OstrichDTO baseOstrich() {
		return OstrichDTO.builder()
						.name("Medium Ostrich")
						.gender(1)
						.weight(65.5f)
						.age(35)
						.height(1.75f)
						.arrivalDate(LocalDateTime.parse("2022-10-15T12:00:00"))
						.build();
	}
	
}