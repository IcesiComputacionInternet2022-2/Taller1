package co.edu.icesi.zoocanyonriver.repository;

import co.edu.icesi.zoocanyonriver.model.Tiger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TigerRepository extends CrudRepository<Tiger, UUID> {}
