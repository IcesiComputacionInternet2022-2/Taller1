package co.edu.icesi.calizoo.api;

import co.edu.icesi.calizoo.dto.HyenaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins ="*")
@RequestMapping("/hyenas")
public interface HyenaAPI {

    @GetMapping("/{hyenaId}")
    public HyenaDTO getHyena(@PathVariable UUID hyenaId);

    @PostMapping()
    public HyenaDTO createHyena(@RequestBody HyenaDTO hyenaDTO);

    @GetMapping
    public List<HyenaDTO> getHyenas();

}
