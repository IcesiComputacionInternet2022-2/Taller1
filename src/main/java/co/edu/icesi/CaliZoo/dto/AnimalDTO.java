package co.edu.icesi.CaliZoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

    private UUID id;

    private String name;

    private String sex;

    private double weight;

    private int age;

    private  double height;

    private Date arrival_date;

    private String [] parents = new String[2];
}
