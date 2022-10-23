package co.edu.icesi.calizoo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hyena {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    private String sex;

    private double weight;

    private int age;

    private int height;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDate;

    @Nullable
    private UUID fatherId;

    @Nullable
    private UUID motherId;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}
