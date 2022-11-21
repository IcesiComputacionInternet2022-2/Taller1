package co.edu.icesi.apizoo.api;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/animals")
public interface ViewControl {

    @GetMapping
    public String zoo(Model model);

    @GetMapping("/index")
    public String getAnimals(Model model);

    @GetMapping("/add")
    public String addAnimal(Model model);

    @PostMapping("/create")
    public String createAnimal(@ModelAttribute AnimalDTO animalDTO,
                        BindingResult bindingResult, Model model, @RequestParam(value = "action", required = true) String action) throws Exception;

    @GetMapping("/edit")
    public String editAnimal(Model model);

    @PostMapping("/update")
    public String updateAnimal(Model model);
}
