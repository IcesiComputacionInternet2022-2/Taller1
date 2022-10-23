package co.edu.icesi.ecozoo.api;

import co.edu.icesi.ecozoo.dto.AnimalResponseDTO;
import co.edu.icesi.ecozoo.dto.CapybaraDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
@Validated
@RequestMapping("/capybaras")
public interface CapybaraAPI {

    @GetMapping("/{capybaraId}")
    public AnimalResponseDTO getAnimal(@Valid @NotNull @PathVariable UUID capybaraId);

    @GetMapping("/name={capybaraName}")
    public AnimalResponseDTO getAnimalByName(@Valid @NotBlank @PathVariable String capybaraName);

    @PostMapping
    public CapybaraDTO createAnimal(@Valid @NotNull @RequestBody CapybaraDTO capybaraDTO);

    @GetMapping
    public List<CapybaraDTO> getAnimals();
}
