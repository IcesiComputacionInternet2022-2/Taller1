package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.dto.TatabroParentsDTO;
import co.edu.icesi.zoo.model.Tatabro;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.UUID;

public interface TatabroService {

    Tatabro getTatabroByID(@PathVariable UUID tatabroId);

    TatabroParentsDTO getTatabroByName(@PathVariable String tatabroName);

    Tatabro createTatabro(@RequestBody Tatabro tatabro);

    List<Tatabro> getTatabros();
}