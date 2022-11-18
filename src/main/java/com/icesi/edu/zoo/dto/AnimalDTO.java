package com.icesi.edu.zoo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String maleParentName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String femaleParentName;

    private char sex;

    private double weight;

    private int age;

    private double height;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrivalDate;

}
