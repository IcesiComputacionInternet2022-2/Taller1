package co.edu.icesi.ecozoo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Animal {
    @Id
    private UUID id;

    private String name;

    private boolean sex;

    private Double weight;

    private int age;

    private Double height;

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;

    @Column(name = "father_id")
    private UUID fatherID;

    @Column(name = "mother_id")
    private UUID motherID;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }
}
