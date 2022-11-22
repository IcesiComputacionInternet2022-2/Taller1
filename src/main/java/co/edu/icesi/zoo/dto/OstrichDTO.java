package co.edu.icesi.zoo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@Data
@Builder
public class OstrichDTO {
	
	private UUID id;
    private String name;
    private int gender;
    private float weight;
    private int age;
    private float height;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalDate;
    @Nullable
    private UUID fatherId;
    @Nullable
    private UUID motherId;
    
}
