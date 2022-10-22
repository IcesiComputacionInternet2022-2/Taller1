package co.icesi.edu.animals.controller;

import co.icesi.edu.animals.api.CaripiareAPI;
import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
import co.icesi.edu.animals.dto.CaripiareDTO;
import co.icesi.edu.animals.mapper.CaripiareMapper;
import co.icesi.edu.animals.service.CaripiareService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CaripiareController implements CaripiareAPI {

    private final CaripiareService caripiareService;
    private final CaripiareMapper caripiareMapper;

    @Override
    public CaripiareDTO createCaripiare(CaripiareDTO caripiareDTO) {
        return caripiareMapper.fromCaripiare(caripiareService.createCaripiare(caripiareMapper.fromCaripiareDTO(caripiareDTO)));
    }

    @Override
    public CaripiareDTO getCaripiare(UUID id) {
        return caripiareMapper.fromCaripiare(caripiareService.getCaripiare(id));
    }

    @Override
    public CaripiareAndParentsDTO getCaripiareAndParents(String name) {
        return caripiareService.getCaripiareAndParents(name);
    }

    @Override
    public List<CaripiareDTO> getCaripiares() {
        return caripiareService.getCaripiares().stream().map(caripiareMapper::fromCaripiare).collect(Collectors.toList());
    }

    @Override
    public CaripiareDTO updateCaripiare(UUID id, CaripiareDTO caripiareDTO) {
        return caripiareMapper.fromCaripiare(caripiareService.updateCaripiare(caripiareMapper.fromCaripiareDTO(id, caripiareDTO)));
    }
}
