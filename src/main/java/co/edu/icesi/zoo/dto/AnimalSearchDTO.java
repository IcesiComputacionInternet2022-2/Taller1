package co.edu.icesi.zoo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalSearchDTO {

    private UUID id;
    private String name;
    private String gender;
    private int age;
    private double weight;
    private double height;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDate;
    @Nullable
    private AnimalNoParentsDTO father;
    @Nullable
    private AnimalNoParentsDTO mother;

}
