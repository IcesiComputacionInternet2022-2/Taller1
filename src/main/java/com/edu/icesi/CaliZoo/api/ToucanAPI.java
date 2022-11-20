package com.edu.icesi.CaliZoo.api;

import com.edu.icesi.CaliZoo.dto.ToucanDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/toucans")
@CrossOrigin(origins = "http://localhost:3000/")
public interface ToucanAPI {

    @GetMapping("/{toucanName}")
    public List<ToucanDTO> getToucan(@PathVariable String toucanName);

    @PostMapping()
    public ToucanDTO createToucan(@RequestBody ToucanDTO toucanDTO);

    @GetMapping
    public List<ToucanDTO> getToucans();

}//End ToucanAPI
