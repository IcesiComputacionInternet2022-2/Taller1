package co.edu.icesi.Zootopia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    //@Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "father_id")
    private UUID fatherId;
    @Column(name = "mother_id")
    private UUID motherId;



    private String name;
    private String sex;

    private float weight;

    private float age;

    private float height;

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;


    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }

}
