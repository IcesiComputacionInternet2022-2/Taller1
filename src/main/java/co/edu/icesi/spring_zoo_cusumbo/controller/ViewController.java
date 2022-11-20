package co.edu.icesi.spring_zoo_cusumbo.controller;


import co.edu.icesi.spring_zoo_cusumbo.api.ViewAPI;
import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.mapper.CusumboMapper;
import co.edu.icesi.spring_zoo_cusumbo.service.CusumboService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ViewController implements ViewAPI {

    private final CusumboService cusumboService;

    private final CusumboMapper cusumboMapper;

    private final CusumboController cusumboController;

    @Override
    public String home(Model model) {
        return "home";
    }

    @Override
    public String createCusumbo(Model model) {
        model.addAttribute("cusumboDTO",new CusumboDTO());
        return "create-cusumbo";
    }

    @Override
    public String saveCusumbo(Model model, CusumboDTO cusumboDTO, String arrivalDate2) {
        cusumboDTO.setArrivalDate(arrivalDate2==null || arrivalDate2.isEmpty()?null:LocalDateTime.parse(arrivalDate2));
        cusumboController.createCusumbo(cusumboDTO);
        return "redirect:/cusumbos/view-cusumbos";
    }


    @Override
    public String viewCusumbo(Model model) {
        model.addAttribute("cusumbos",getCusumbos());
        return "view-cusumbos";
    }

    @Override
    public String findCusumbo(Model model, String cusumboName) {
        model.addAttribute("cusumbosFamily",cusumboName != null? getCusumboWithParents(cusumboName):new ArrayList<CusumboDTO>());

        return "find-cusumbo";
    }

    private List<CusumboDTO> getCusumbos() {//Same as CusumboController method
        return cusumboService.getCusumbos().stream().map(cusumboMapper::fromCusumbo).collect(Collectors.toList());
    }

    private  List<CusumboDTO> getCusumboWithParents(String cusumboName) {
        return cusumboService.getCusumboWithParents(cusumboName).stream().map(cusumboMapper::fromCusumbo).collect(Collectors.toList());
    }
}
