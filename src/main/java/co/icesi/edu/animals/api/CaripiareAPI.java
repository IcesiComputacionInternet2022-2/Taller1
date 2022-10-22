package co.icesi.edu.animals.api;

import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
import co.icesi.edu.animals.dto.CaripiareDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RequestMapping("/caripiares")
public interface CaripiareAPI {

    @PostMapping()
    CaripiareDTO createCaripiare(@RequestBody @NotNull @Valid CaripiareDTO caripiareDTO);

    @GetMapping("/id/{id}")
    CaripiareDTO getCaripiare(@PathVariable UUID id);

    @GetMapping("/name/{name}")
    CaripiareAndParentsDTO getCaripiareAndParents(@PathVariable String name);
    
    @GetMapping
    List<CaripiareDTO> getCaripiares();

    @PostMapping("/id/{id}")
    CaripiareDTO updateCaripiare(@PathVariable @NotNull UUID id, @NotNull @Valid @RequestBody CaripiareDTO caripiareDTO);
}
