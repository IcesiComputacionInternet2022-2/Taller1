package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.dto.TatabroParentsDTO;
import co.edu.icesi.zoo.error.exception.TatabroException;
import co.edu.icesi.zoo.mapper.TatabroMapper;
import co.edu.icesi.zoo.service.TatabroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class ViewController {

    private final TatabroService tatabroService;

    private final TatabroMapper tatabroMapper;

    @GetMapping("/index")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/createTatabroPage")
    public String createTatabro(Model model) {
        model.addAttribute("tatabroDTO", new TatabroDTO());
        return "createTatabro";
    }

    @PostMapping("/createTatabro")
    public String createTatabro(@Valid @ModelAttribute TatabroDTO tatabroDTO, BindingResult errors, Model model) {
        if (!hasBindingErrors(errors, model)) {
            try {
                tatabroService.createTatabro(tatabroMapper.fromDTO(tatabroDTO));
                model.addAttribute("tatabroResponse", true);
            } catch (TatabroException tatabroException) {
                model.addAttribute("tatabroResponse", false);
                model.addAttribute("message", tatabroException.getError().getMessage());
            }
        }
        return "createTatabro";
    }

    private boolean hasBindingErrors(BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("tatabroResponse", false);
            model.addAttribute("message", Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
            return true;
        }
        return false;
    }

    @GetMapping("/searchTatabroPage")
    public String searchTatabro(Model model) {
        model.addAttribute("tatabroParentsDTO", new TatabroParentsDTO());
        return "searchTatabro";
    }

    @GetMapping("/searchTatabro")
    public String searchTatabro(@RequestParam(value = "tatabroName", required = false) String tatabroName, Model model) {
        try {
            model.addAttribute("tatabroParentsDTO", tatabroService.getTatabroByName(tatabroName));
        } catch (TatabroException tatabroException) {
            model.addAttribute("tatabroParentsDTO", new TatabroParentsDTO());
            model.addAttribute("tatabroResponse", false);
            model.addAttribute("message", tatabroException.getError().getMessage());
        }
        return "searchTatabro";
    }

    @GetMapping("/getTatabros")
    public String getTatabros(Model model) {
        model.addAttribute("tatabros", tatabroService.getTatabros());
        return "getTatabros";
    }
}