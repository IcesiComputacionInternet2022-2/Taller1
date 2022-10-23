package co.edu.icesi.calizoowebapp.repository;

import co.edu.icesi.calizoowebapp.model.AfricanLion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AfricanLionRespository extends CrudRepository<AfricanLion, UUID> {
}
