package co.edu.icesi.zoo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.icesi.zoo.model.Ostrich;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OstrichRepository extends CrudRepository<Ostrich, UUID> {
	
	Optional<Ostrich> findByName(String name);
	
}
