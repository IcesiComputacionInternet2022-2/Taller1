package co.edu.icesi.ecozoo.dto;

import co.edu.icesi.ecozoo.constant.AnimalSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

    private UUID id;

    @NotNull(message = "The name of the animal cannot be null")
    @Size(min = 1, max = 120, message = "The name of the animal must be between 1 and 120 characters")
    @Pattern(regexp = "[a-zA-Z]+\\w*[a-zA-Z]+", message = "The name of the animal can only contain letters and numbers")
    private String name;

    @NotNull(message = "The sex of the animal cannot be null")
    private AnimalSex sex;

    private Double weight;

    @NotNull(message = "The age of the animal cannot be null")
    private int age;

    private Double height;

    @NotNull(message = "The arrival date cannot be null")
    @PastOrPresent(message = "The arrival date must be a past or present date")
    private LocalDateTime arrivalDate;

    private UUID motherID;

    private UUID fatherID;
}
