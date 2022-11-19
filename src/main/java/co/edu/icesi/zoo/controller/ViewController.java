package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
@AllArgsConstructor
public class ViewController {

    private final AnimalService animalService;


}
