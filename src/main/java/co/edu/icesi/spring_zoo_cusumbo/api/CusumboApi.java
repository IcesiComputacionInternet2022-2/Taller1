package co.edu.icesi.spring_zoo_cusumbo.api;

import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/animals/cusumbo")
public interface CusumboApi {

    @GetMapping("/{cusumboName}")
    public List<CusumboDTO> getCusumboWithParents(@PathVariable String cusumboName);

    @PostMapping()
    public CusumboDTO createCusumbo(@RequestBody CusumboDTO cusumboDTO);

    @GetMapping
    public List<CusumboDTO> getCusumbos();




}
