package co.edu.icesi.zoo.dto;

import co.edu.icesi.zoo.model.Animal;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnimalWithParentsDTO {

    private String animalId;
    private String name;
    private char sex;
    private double weight;
    private double age;
    private double height;
    private String arrivalDate;
    private Animal motherInfo;
    private Animal fatherInfo;

}
