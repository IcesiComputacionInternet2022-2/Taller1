package com.co.edu.icesi.zooWeb.repository;

import com.co.edu.icesi.zooWeb.model.BlackSwan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlackSwanRepository extends CrudRepository<BlackSwan, UUID> {
}
