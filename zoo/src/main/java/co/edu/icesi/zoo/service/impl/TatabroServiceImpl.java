package co.edu.icesi.zoo.service.impl;

import co.edu.icesi.zoo.constant.TatabroErrorCode;
import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.dto.TatabroParentsDTO;
import co.edu.icesi.zoo.error.exception.TatabroError;
import co.edu.icesi.zoo.error.exception.TatabroException;
import co.edu.icesi.zoo.mapper.TatabroMapper;
import co.edu.icesi.zoo.model.Tatabro;
import co.edu.icesi.zoo.repository.TatabroRepository;
import co.edu.icesi.zoo.service.TatabroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class TatabroServiceImpl implements TatabroService {

    public final TatabroRepository tatabroRepository;
    public final TatabroMapper tatabroMapper;

    @Override
    public Tatabro getTatabroByID(UUID tatabroId) {
        return tatabroRepository.findById(tatabroId).orElseThrow(() -> new TatabroException(HttpStatus.BAD_REQUEST, new TatabroError(TatabroErrorCode.CODE_01, TatabroErrorCode.CODE_01.getMessage())));
    }

    @Override
    public TatabroParentsDTO getTatabroByName(String tatabroName) {
        TatabroDTO child = tatabroMapper.fromTatabro(tatabroRepository.findByName(tatabroName).orElseThrow(() -> new TatabroException(HttpStatus.BAD_REQUEST, new TatabroError(TatabroErrorCode.CODE_02, TatabroErrorCode.CODE_02.getMessage()))));
        TatabroDTO father = tatabroMapper.fromTatabro(Optional.ofNullable(child.getFatherID()).map(this::getTatabroByID).orElse(null));
        TatabroDTO mother = tatabroMapper.fromTatabro(Optional.ofNullable(child.getMotherID()).map(this::getTatabroByID).orElse(null));
        return tatabroMapper.fromTatabroDTO(child, father, mother);
    }

    @Override
    public Tatabro createTatabro(Tatabro tatabro) {
        validateParents(Optional.ofNullable(tatabro.getFatherID()), Optional.ofNullable(tatabro.getMotherID()));
        validateUniqueName(tatabro.getName());
        return tatabroRepository.save(tatabro);
    }

    @Override
    public List<Tatabro> getTatabros() {
        return StreamSupport.stream(tatabroRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    private void validateUniqueName(String tatabroName) {
        if (getTatabros().stream().anyMatch(tatabro -> tatabro.getName().equals(tatabroName)))
            throw new TatabroException(HttpStatus.BAD_REQUEST, new TatabroError(TatabroErrorCode.CODE_03, TatabroErrorCode.CODE_03.getMessage()));
    }

    private void validateParents(Optional<UUID> fatherID, Optional<UUID> motherID) {
        fatherID.map(this::getTatabroByID).ifPresent(father -> {
            if (!father.getSex().matches("[Mm]"))
                throw new TatabroException(HttpStatus.BAD_REQUEST, new TatabroError(TatabroErrorCode.CODE_04, TatabroErrorCode.CODE_04.getMessage()));
        });
        motherID.map(this::getTatabroByID).ifPresent(mother -> {
            if (!mother.getSex().matches("[Ff]"))
                throw new TatabroException(HttpStatus.BAD_REQUEST, new TatabroError(TatabroErrorCode.CODE_04, TatabroErrorCode.CODE_04.getMessage()));
        });
    }
}