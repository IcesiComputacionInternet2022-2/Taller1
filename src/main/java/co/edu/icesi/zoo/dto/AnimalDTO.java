package co.edu.icesi.zoo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnimalDTO {


    private String animalId;

    @NotBlank
    @Size(min = 1, max = 120)
    private String name;
    private char sex;
    private double weight;
    private double age;
    private double height;
    private String arrivalDate;
    private String motherId;
    private String fatherId;


}
