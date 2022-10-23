package co.edu.icesi.zoocanyonriver.service;

import co.edu.icesi.zoocanyonriver.dto.TigerResponseDTO;
import co.edu.icesi.zoocanyonriver.model.Tiger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface TigerService {

    public TigerResponseDTO getTiger(@PathVariable String tigerName);

    public Tiger getTiger(@PathVariable UUID uuidTiger);

    public Tiger createTiger(@RequestBody Tiger tiger);

    public List<Tiger> getTigers();

}
