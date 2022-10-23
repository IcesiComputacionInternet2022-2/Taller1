package co.edu.icesi.zoo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "tatabros")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tatabro {

    @Id
    private UUID id;

    private String name;

    private String sex;

    private double weight;

    private int age;

    private double height;

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;

    @Column(name = "father_id")
    private UUID fatherID;

    @Column(name = "mother_id")
    private UUID motherID;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}