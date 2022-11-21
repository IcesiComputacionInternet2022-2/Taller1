package co.edu.icesi.zoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TatabroDTO {

    private UUID id;

    @NotNull(message = "Attribute 'name' cannot be null.")
    @Pattern(regexp = "[a-zA-Z ]{1,120}", message = "Names cannot exceed 120 digits and can only contain letters and spaces.")
    private String name;

    @NotNull(message = "Attribute 'sex' cannot be null.")
    @Pattern(regexp = "[MmFf]", message = "Tatabro's sex doesn't match with M/m (Male) or F/f (Female).")
    private String sex;

    @NotNull(message = "Attribute 'weight' cannot be null.")
    @DecimalMin(value = "20.0", message = "Tatabro's weight doesn't correspond to tatabro's weight average (between 20 kg and 40 kg).")
    @DecimalMax(value = "40.0", message = "Tatabro's weight doesn't correspond to tatabro's weight average (between 20 kg and 40 kg).")
    private double weight;

    @NotNull(message = "Attribute 'age' cannot be null.")
    @Range(min = 15, max = 20, message = "Tatabro's age doesn't correspond to tatabro's age average (between 15 and 20 years old).")
    private int age;

    @NotNull(message = "Attribute 'height' cannot be null.")
    @DecimalMin(value = "50.0", message = "Tatabro's height doesn't correspond to tatabro's height average (between 50 cm and 90 cm)")
    @DecimalMax(value = "90.0", message = "Tatabro's height doesn't correspond to tatabro's height average (between 50 cm and 90 cm)")
    private double height;

    @NotNull(message = "Attribute 'arrivalDate' cannot be null.")
    @PastOrPresent(message = "The arrival date cannot be after the current date and time.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalDate;

    private UUID fatherID;

    private UUID motherID;
}