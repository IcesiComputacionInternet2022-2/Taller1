package co.edu.icesi.zoologico.dto;

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

    private LocalDateTime arrivalDate;



}
