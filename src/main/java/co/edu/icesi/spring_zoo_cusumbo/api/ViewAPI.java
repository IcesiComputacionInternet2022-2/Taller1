package co.edu.icesi.spring_zoo_cusumbo.api;

import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public interface ViewAPI {

    @GetMapping("/cusumbos/create-cusumbo")
    String createCusumbo(Model model);

    @PostMapping("/cusumbos/create-cusumbo/save")
    String saveCusumbo(Model model, CusumboDTO cusumboDTO, @RequestParam String arrivalDate);

    @GetMapping("/cusumbos/view-cusumbos")
    String viewCusumbo(Model model);

    @GetMapping("/home")
    String home(Model model);

    @GetMapping("/cusumbos/find-cusumbo")
    String findCusumbo(Model model, String cusumboName);

    @GetMapping()
    default String homeRedirect(){
        return "redirect:/home";
    }
}
