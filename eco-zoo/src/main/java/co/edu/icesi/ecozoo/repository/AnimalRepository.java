package co.edu.icesi.ecozoo.repository;

import co.edu.icesi.ecozoo.model.Animal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, UUID> {
    Optional<Animal> findByName(String name);
}
