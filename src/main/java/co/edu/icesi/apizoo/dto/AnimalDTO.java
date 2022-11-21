package co.edu.icesi.apizoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

    private UUID id;

    private String name;

    private String sex;

    private Integer weight;

    private Integer age;

    private Integer height;

    private LocalDateTime arrivalDate;

    private UUID fatherID;

    private UUID motherID;


}
