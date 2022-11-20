package com.edu.icesi.CaliZoo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToucanDTO {

    private UUID id;
    private String fatherName;
    private String motherName;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 120)
    private String name;
    @NotBlank
    @NotNull
    @Size(min =1, max = 1)
    private String sex;
    @NotNull
    @Max(value = 680)
    @Min(value = 130)
    private double weight;
    @NotNull
    @Max(value = 20)
    @Min(value = 0)
    private int age;
    @NotNull
    @Max(value = 65)
    @Min(value = 18)
    private double height;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfArrival;

}//End ToucanDTO
