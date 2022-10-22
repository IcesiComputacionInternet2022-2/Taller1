package co.icesi.edu.animals.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaripiareDTO {

    public static final String NAME_REGEX = "[a-zA-Z][a-zA-Z ]+[a-zA-Z]$";
    public static final int MAX_NAME_LENGTH = 120;
    public final static String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    public static final String MALE_GENDER_REGEX = "[Mm]";
    public static final String FEMALE_GENDER_REGEX = "[Ff]";
    public static final String GENDER_REGEX = "[MmFf]";

    //Kilograms
    public static final String MIN_WEIGHT = "1.0";
    public static final String MAX_WEIGHT = "5.0";

    //Years
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 20;

    //Meters
    public static final String MIN_HEIGHT = "0.1";
    public static final String MAX_HEIGHT = "0.2";

    private UUID id;

    @NotEmpty(message = "'name' may not be empty.")
    @Pattern(regexp = NAME_REGEX, message = "'name' may only contain letters and spaces.")
    @Size(max = MAX_NAME_LENGTH, message = "'name' may not be longer than 120 characters.")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID fatherId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID motherId;

    @NotEmpty(message = "'gender' may not be empty.")
    @Pattern(regexp = GENDER_REGEX, message = "'gender' may only be 'M' or 'm' for male and 'F' or 'f' for female.")
    private String gender;

    @NotNull(message = "'weight' may not be null.")
    @DecimalMin(value = MIN_WEIGHT, message = "'weight' may not be lower than 1.0 kilograms.")
    @DecimalMax(value = MAX_WEIGHT, message = "'weight' may not be higher than 5.0 kilograms.")
    private double weight;

    @NotNull(message = "'age' may not be null")
    @Range(min = MIN_AGE, max = MAX_AGE, message = "'age' may not be lower than 0 years or higher than 20 years.")
    private int age;

    @NotNull(message = "'height' may not be null.")
    @DecimalMin(value = MIN_HEIGHT, message = "'height' may not be lower than 0.1 meters.")
    @DecimalMax(value = MAX_HEIGHT, message = "'height' may not be higher than 0.2 meters.")
    private double height;

    @NotNull(message = "'arrivalDate' may not be null.")
    @PastOrPresent(message = "'arrivalDate' may not come after the current date.")
    private LocalDate arrivalDate;
}