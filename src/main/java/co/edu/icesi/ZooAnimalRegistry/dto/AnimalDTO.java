package co.edu.icesi.ZooAnimalRegistry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

    private UUID id;
    @NotEmpty(message ="can not be empty")
    @Size(min=4, max=120, message="must be between 4 and 120 characters")
    private String name;
    @NotEmpty(message ="can not be empty")
    private String sex;
    @NotNull(message ="can not be empty")
    @Range(min=1,max=100,message ="does not have a valid range for the species")
    private double weight;
    @NotNull(message ="can not be empty")
    @Range(min=1,max=14,message ="does not have a valid range for the species")
    private int age;
    @NotNull(message ="can not be empty")
    @Range(min=20,max=90,message ="does not have a valid range for the species")
    private double height;
    @NotEmpty(message ="can not be empty")
    private String date;
    private String motherId;
    private String fatherId;
}
