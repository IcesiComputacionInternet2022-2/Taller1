package co.edu.icesi.ZooAnimalRegistry.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column(unique = true)
    private String name;
    private String sex;
    private double weight;
    private int age;
    private double height;
    private String date;
    private String motherId;
    private String fatherId;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}
