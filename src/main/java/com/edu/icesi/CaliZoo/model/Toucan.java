package com.edu.icesi.CaliZoo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table(name = "`animal`")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Toucan {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private String fatherName;
    private String motherName;
    private String name;
    private String sex;
    private double weight;
    private int age;
    private double height;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dateOfArrival;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }

}//End animal
