package co.edu.icesi.apizoo.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    private String sex;

    private Integer weight;

    private Integer age;

    private Integer height;

    private LocalDateTime arrivalDate;

    private UUID father;

    private UUID mother;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}
