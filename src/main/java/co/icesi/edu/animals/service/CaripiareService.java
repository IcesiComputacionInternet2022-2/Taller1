package co.icesi.edu.animals.service;

import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
import co.icesi.edu.animals.model.Caripiare;

import java.util.List;
import java.util.UUID;

public interface CaripiareService {

    Caripiare createCaripiare(Caripiare Caripiare);

    Caripiare getCaripiare(UUID id);

    CaripiareAndParentsDTO getCaripiareAndParents(String name);

    List<Caripiare> getCaripiares();

    Caripiare updateCaripiare(Caripiare caripiare);
}
