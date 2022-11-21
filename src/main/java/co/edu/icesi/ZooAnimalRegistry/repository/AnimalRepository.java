package co.edu.icesi.ZooAnimalRegistry.repository;

import co.edu.icesi.ZooAnimalRegistry.repository.model.Animal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, UUID> {
}
