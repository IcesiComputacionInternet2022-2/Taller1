package co.edu.icesi.zoo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name="animal")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Animal {

    @Id

    //@Type(type="uuid-char")
    //@Type(type="org.hibernate.type.UUIDCharType") Same thing as above
    private UUID animalId;

    private String name;
    private char sex;
    private double weight;
    private double age;
    private double height;
    private LocalDateTime arrivalDate;

    //@Type(type="uuid-char")
    //@Type(type="org.hibernate.type.UUIDCharType") Same thing as above
    private UUID motherId;

    //@Type(type="uuid-char")
    //@Type(type="org.hibernate.type.UUIDCharType") Same thing as above
    private UUID fatherId;

    @PrePersist
    public void generateId(){
        this.animalId = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "{\n id:"+animalId+"\n name:"+name+"\n sex:"+sex+"\n weight:"+weight+" \n age:"+age+"\n height:"+height+"\n arrivalDate:"+arrivalDate+"\n motherId:"+motherId+"\n father:"+fatherId+" \n}";
    }

}
