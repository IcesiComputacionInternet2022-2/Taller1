package co.edu.icesi.zoo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.icesi.zoo.model.Ostrich;

public interface OstrichService {
	
	Ostrich getOstrichById(@PathVariable UUID ostrichId);
	
	List<Ostrich> getOstrichByName(@PathVariable String ostrichName);

    Ostrich createOstrich(@RequestBody Ostrich ostrich);

    List<Ostrich> getOstriches();
    
}
