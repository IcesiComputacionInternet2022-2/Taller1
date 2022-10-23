package co.edu.icesi.calizoowebapp.model;

import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "`african_lion`")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AfricanLion {

    @Id
    @NotNull
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AnimalSex sex;

    private double weight;

    private int age;

    private double height;

    private LocalDateTime arrivedZooDate;

    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID fatherId;

    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID motherId;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

}
