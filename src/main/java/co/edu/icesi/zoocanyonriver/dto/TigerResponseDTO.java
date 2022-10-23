package co.edu.icesi.zoocanyonriver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TigerResponseDTO {
    private UUID id;

    private String name;

    private String gender;

    private double weight;

    private String age;

    private String height;

    private LocalDateTime arriveDate;

    private TigerDTO mother;

    private TigerDTO father;
}
