package com.co.edu.icesi.zooWeb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "SWAN")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackSwan {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    private char sex;

    private double weight;

    private int age;

    private double height;

    @Column(name = "arrival_date")
    private LocalDateTime arrivedZooDate;

    @Column(name = "fatherid")
    private UUID fatherId;

    @Column(name = "motherid")
    private UUID motherId;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

}
