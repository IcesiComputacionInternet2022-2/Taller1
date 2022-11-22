package co.edu.icesi.zoo.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "`ostrich`")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ostrich {

    @Type(type = "org.hibernate.type.UUIDCharType")
    @Id
    private UUID id;
    private String name;
    private int gender;
    private float weight;
    private int age;
    private float height;
    private LocalDateTime arrivalDate;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID fatherId;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID motherId;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

}
