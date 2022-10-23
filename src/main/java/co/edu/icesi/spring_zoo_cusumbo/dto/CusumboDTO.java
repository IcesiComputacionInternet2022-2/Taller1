package co.edu.icesi.spring_zoo_cusumbo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CusumboDTO {

    private UUID id;

    private String name;

    private char sex;

    private float weight;

    private int age;

    private float height;

    private LocalDateTime arrivalDate;

    @Nullable
    private UUID fatherId;

    @Nullable
    private UUID motherId;

}
