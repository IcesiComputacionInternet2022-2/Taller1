package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.api.TatabroAPI;
import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.dto.TatabroParentsDTO;
import co.edu.icesi.zoo.mapper.TatabroMapper;
import co.edu.icesi.zoo.service.TatabroService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class TatabroController implements TatabroAPI {

    public final TatabroService tatabroService;
    public final TatabroMapper tatabroMapper;

    @Override
    public TatabroDTO getTatabroByID(UUID tatabroId) {
        return tatabroMapper.fromTatabro(tatabroService.getTatabroByID(tatabroId));
    }

    @Override
    public TatabroParentsDTO getTatabroByName(String tatabroName) {
        return tatabroService.getTatabroByName(tatabroName);
    }

    @Override
    public TatabroDTO createTatabro(@Valid TatabroDTO tatabroDTO) {
        return tatabroMapper.fromTatabro(tatabroService.createTatabro(tatabroMapper.fromDTO(tatabroDTO)));
    }

    @Override
    public List<TatabroDTO> getTatabros() {
        return tatabroService.getTatabros().stream().map(tatabroMapper::fromTatabro).collect(Collectors.toList());
    }
}