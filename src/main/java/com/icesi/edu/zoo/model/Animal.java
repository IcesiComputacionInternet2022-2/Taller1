package com.icesi.edu.zoo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Table(name = "`animal`")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Id
    private String name;

    //@Type(type="org.hibernate.type.UUIDCharType")
    private String maleParentName;

    //@Type(type="org.hibernate.type.UUIDCharType")
    private String femaleParentName;

    private char sex;

    private double weight;

    private int age;

    private double height;

    private Date arrivalDate;

    @Override
    public String toString() {
        return id + " " + name + " " + maleParentName + " " + femaleParentName;
    }

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }
}
