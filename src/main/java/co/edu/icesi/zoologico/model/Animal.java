package co.edu.icesi.zoologico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Table(name = "`animal`")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private String name;
    private String gender;
    private Integer weight;
    private Integer age;
    private Integer height;
    private UUID mother;
    private UUID father;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalDate;


    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}
