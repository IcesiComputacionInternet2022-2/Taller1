package icesi.edu.co.zoodemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuricatoParentsIdDTO {
    private UUID id;
    private String name;
    private String gender;
    private float weight;
    private int age;
    private float height;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arriveDate;
    private UUID fatherId;
    private UUID motherId;
  //  14d96594-9250-4a59-8423-2d2f461d673d
   // 6e13e2e8-1e1e-4e07-a700-76b3e3c4c95f
}
