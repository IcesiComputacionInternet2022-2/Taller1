package icesi.edu.co.zoodemo.api;

import icesi.edu.co.zoodemo.dto.SuricatoDTO;
import icesi.edu.co.zoodemo.dto.SuricatoParentsDTO;
import icesi.edu.co.zoodemo.dto.SuricatoParentsIdDTO;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "*")
@RequestMapping("/suricato")
public interface SuricatoAPI {
    @PostMapping()
    public SuricatoDTO createSuricato(@RequestBody SuricatoParentsIdDTO suricatoParentsIdDTO);

    @GetMapping()
    public List<SuricatoDTO> getSuricatos();

    @GetMapping("/{suricatoId}")
    public SuricatoParentsDTO getSuricato(@PathVariable UUID suricatoId);


}
