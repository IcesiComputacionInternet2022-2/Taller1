package co.edu.icesi.zoo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OstrichConstant {

	WEIGHT(63, 140),
    AGE(0, 75),
    HEIGHT(1.70f, 2.80f),
    GENDER(0, 1);

    private final float min, max;
    
}
