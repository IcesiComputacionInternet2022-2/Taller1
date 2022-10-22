package co.icesi.edu.animals.repository;

import co.icesi.edu.animals.model.Caripiare;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaripiareRepository extends CrudRepository<Caripiare, UUID>{
}
