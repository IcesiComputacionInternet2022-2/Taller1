package co.edu.icesi.calizoowebapp.dto;

import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfricanLionQueryResponseDTO {

    private AfricanLionDTO requestedLion;

    private AfricanLionDTO lionFather;

    private AfricanLionDTO lionMother;

}
