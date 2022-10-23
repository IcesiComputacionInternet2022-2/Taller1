package co.edu.icesi.ecozoo.dto;

import co.edu.icesi.ecozoo.constant.CapybaraConstraints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapybaraDTO extends AnimalDTO {

    @DecimalMax(value = CapybaraConstraints.MAX_WEIGHT, message = "The weight of the animal must be less or equal than " + CapybaraConstraints.MAX_WEIGHT)
    @DecimalMin(value = CapybaraConstraints.MIN_WEIGHT, message = "The weight of the animal must be greater or equal than " + CapybaraConstraints.MIN_WEIGHT)
    private Double weight;

    @Range(min = CapybaraConstraints.MIN_AGE, max = CapybaraConstraints.MAX_AGE, message = "The age of the animal must be between " + CapybaraConstraints.MIN_AGE + " and " + CapybaraConstraints.MAX_AGE)
    private int age;

    @DecimalMax(value = CapybaraConstraints.MAX_HEIGHT, message = "The height of the animal must be less or equal than " + CapybaraConstraints.MAX_HEIGHT)
    @DecimalMin(value = CapybaraConstraints.MIN_HEIGHT, message = "The height of the animal must be greater or equal than " + CapybaraConstraints.MIN_HEIGHT)
    private Double height;
}
