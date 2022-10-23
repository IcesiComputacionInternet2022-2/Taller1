package co.edu.icesi.calizoowebapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AfricanLionQueryResponse {

    private AfricanLion requestedLion;

    private AfricanLion lionFather;

    private AfricanLion lionMother;
}
