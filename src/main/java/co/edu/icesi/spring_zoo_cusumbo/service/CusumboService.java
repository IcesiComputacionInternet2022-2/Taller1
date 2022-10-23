package co.edu.icesi.spring_zoo_cusumbo.service;

import co.edu.icesi.spring_zoo_cusumbo.model.Cusumbo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface CusumboService {

    List<Cusumbo> getCusumboWithParents(@PathVariable String cusumboName);

    public Cusumbo createCusumbo(@RequestBody Cusumbo cusumbo);


    public List<Cusumbo> getCusumbos();


}
