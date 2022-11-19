package co.edu.icesi.zoo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnimalNoParentsDTO {

    private String animalId;
    private String name;
    private char sex;
    private double weight;
    private double age;
    private double height;
    private String arrivalDate;


}
