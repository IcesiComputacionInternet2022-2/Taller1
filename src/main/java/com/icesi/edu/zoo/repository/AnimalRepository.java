package com.icesi.edu.zoo.repository;

import com.icesi.edu.zoo.model.Animal;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AnimalRepository extends CrudRepository<Animal, String> {
}
