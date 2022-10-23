package co.edu.icesi.calizoowebapp.dto;

import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfricanLionDTO {

    private UUID id;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 120, message = "Lion's name exceeds 120 chars")
    @Pattern(regexp = "[\\sa-zA-Z]+", message = "Only letters and spaces are allowed in lion's name")
    private String name;

    @NotNull
    @NotEmpty
    private AnimalSex sex;

    @NotNull
    @NotEmpty
    @Min(0)
    private double weight;

    @NotNull
    @NotEmpty
    @Min(0)
    private int age;

    @NotNull
    @NotEmpty
    @Min(0)
    private double height;

    @NotNull
    @NotEmpty
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivedZooDate;

    @NotEmpty
    private UUID fatherId;

    @NotEmpty
    private UUID motherId;

}
