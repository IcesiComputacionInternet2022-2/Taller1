package co.edu.icesi.zoo.repository;

import co.edu.icesi.zoo.model.Animal;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AnimalRepository extends CrudRepository<Animal, UUID> {


}
