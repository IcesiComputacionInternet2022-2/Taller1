package co.edu.icesi.ecozoo.dto;

import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.model.Animal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AnimalResponseDTO {

    private UUID id;

    private String name;

    private AnimalSex sex;

    private Double weight;

    private int age;

    private Double height;

    private LocalDateTime arrivalDate;

    @Valid
    private Animal mother;

    @Valid
    private Animal father;
}
