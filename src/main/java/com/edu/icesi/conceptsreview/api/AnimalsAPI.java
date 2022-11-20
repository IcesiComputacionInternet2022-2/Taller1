package com.edu.icesi.conceptsreview.api;


import com.edu.icesi.conceptsreview.dto.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/animals")
public interface AnimalsAPI {

    @GetMapping
    public String getIndex(Model model);

    @PostMapping("/create")
    public String createAnimal(@ModelAttribute AnimalParentsDTO animalParentsDTO, Model model);

    @GetMapping("/create")
    public String getCreateAnimalPage(Model model);

    @GetMapping("/animals")
    public String getAnimals(Model model);

    @GetMapping("/searchAnimal/{animalId}")
    public String getAnimal(@PathVariable UUID animalId, Model model);

    @GetMapping("/searchAnimal")
    public String getSearchAnimalPage(Model model);

}
