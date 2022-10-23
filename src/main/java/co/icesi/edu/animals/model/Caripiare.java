package co.icesi.edu.animals.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table(name = "`caripiare`")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Caripiare {

//    @Type(type = "uuid-char")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Id
    private UUID id;

    private String name;

    private String gender;

    private double weight;

    private int age;

    private double height;

    private LocalDate arrivalDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID fatherId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID motherId;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString(){
        return this.name + " " + this.fatherId + " " + this.motherId + " " + this.gender + " " + this.weight
                + " " + this.age + " " + this.height + " " + this.arrivalDate;
    }
}
