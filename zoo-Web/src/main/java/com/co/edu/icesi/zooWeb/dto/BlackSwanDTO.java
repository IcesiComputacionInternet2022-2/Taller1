package com.co.edu.icesi.zooWeb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackSwanDTO {

    private UUID id;

    private String name;

    private char sex;

    private double weight;

    private int age;

    private double height;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivedZooDate;

    private UUID fatherId;

    private UUID motherId;

}
