package co.edu.icesi.zoologico.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AnimalParentsDTO {
    private UUID id;

    private String name;
    private String gender;
    private Integer weight;
    private Integer age;
    private Integer height;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDate;



}
