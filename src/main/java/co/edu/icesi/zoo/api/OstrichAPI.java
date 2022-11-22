package co.edu.icesi.zoo.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import co.edu.icesi.zoo.dto.OstrichDTO;

@RequestMapping("/ostrich")
public interface OstrichAPI {

    @PostMapping
    OstrichDTO createOstrich(@RequestBody OstrichDTO ostrichDTO);

    @GetMapping("/{ostrichId}")
    OstrichDTO getOstrichById(@PathVariable UUID ostrichId);
    
    @GetMapping("/name/{ostrichName}")
    List<OstrichDTO> getOstrichByName(@PathVariable String ostrichName);

    @GetMapping
    List<OstrichDTO> getOstriches();
}
