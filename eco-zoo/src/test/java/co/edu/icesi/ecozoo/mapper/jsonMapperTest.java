package co.edu.icesi.ecozoo.mapper;

import co.edu.icesi.ecozoo.config.jackson.LocalDateTimeDeserializer;
import co.edu.icesi.ecozoo.config.jackson.LocalDateTimeSerializer;
import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.dto.CapybaraDTO;
import co.edu.icesi.ecozoo.model.Animal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class jsonMapperTest {
    @Test
    public void testObjectMapper() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(module);

        String text = "{\"id\":\"9efab07b-b612-424c-b7b3-72a7bc82d836\",\"name\":\"Tralfagar D. Law\",\"sex\":\"MALE\",\"arrivalDate\":\"2022-01-01T00:00\"}";
        var animalFromText = objectMapper.readValue(text, CapybaraDTO.class);
        System.out.println(animalFromText);
        var animal = Animal.builder().id(UUID.fromString("9efab07b-b612-424c-b7b3-72a7bc82d836")).name("Tralfagar D. Law").sex(AnimalSex.MALE.isValue()).arrivalDate(LocalDateTime.now()).build();
        System.out.println(animal);
        System.out.println(objectMapper.writeValueAsString(animal));
    }

}
