package co.edu.icesi.zoologico.dto;

import co.edu.icesi.zoologico.config.jackson.LocalDateTimeDeserializer;
import co.edu.icesi.zoologico.config.jackson.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AnimalDTO {
    private UUID id;

    private String name;
    private String gender;
    private Integer weight;
    private Integer age;
    private Integer height;
    private UUID mother;
    private UUID father;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDate;


}
