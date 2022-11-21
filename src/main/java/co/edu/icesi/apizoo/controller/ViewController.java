package co.edu.icesi.apizoo.controller;

import co.edu.icesi.apizoo.api.AnimalAPI;
import co.edu.icesi.apizoo.api.ViewControl;
import co.edu.icesi.apizoo.dto.AnimalDTO;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
@AllArgsConstructor
public class ViewController  implements ViewControl {

    @Autowired
    private AnimalAPI animalAPI;

    @Override
    public String zoo(Model model) {
        return "zoo";
    }

    @Override
    public String getAnimals(Model model){
        model.addAttribute("animals", animalAPI.getAnimals());
        return "animals-index";
    }

    @Override
    public String addAnimal(Model model) {
        model.addAttribute("animal", new AnimalDTO());
        model.addAttribute("date", new Date());
        return "create-animal";
    }

    @Override
    public String createAnimal(@ModelAttribute AnimalDTO animalDTO,
                               BindingResult bindingResult, Model model, @RequestParam(value = "action", required = true) @NotNull String action) throws Exception{
        if (action.equals("Cancel")) {
            return "redirect:/animals/index";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("animal", new AnimalDTO());
            return "create-animal";
        } else {
            this.animalAPI.createAnimal(animalDTO);
            return "redirect:/animal/index";
        }
    }

    @Override
    public String editAnimal(Model model) {
        return "update-animal";
    }

    @Override
    public String updateAnimal(Model model) {
        return "redirect:/animals/index";
    }

}
