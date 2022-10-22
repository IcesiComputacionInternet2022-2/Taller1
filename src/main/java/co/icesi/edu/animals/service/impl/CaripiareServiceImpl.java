package co.icesi.edu.animals.service.impl;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
import co.icesi.edu.animals.dto.CaripiareDTO;
import co.icesi.edu.animals.error.exception.CaripiareError;
import co.icesi.edu.animals.error.exception.CaripiareException;
import co.icesi.edu.animals.mapper.CaripiareMapper;
import co.icesi.edu.animals.model.Caripiare;
import co.icesi.edu.animals.repository.CaripiareRepository;
import co.icesi.edu.animals.service.CaripiareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CaripiareServiceImpl implements CaripiareService {

    private final CaripiareRepository caripiareRepository;
    private final CaripiareMapper caripiareMapper;

    @Override
    public Caripiare createCaripiare(Caripiare caripiare) {
        validateUniqueName(caripiare.getName());
        validateParentsGender(caripiare.getFatherId(), caripiare.getMotherId());
        return caripiareRepository.save(caripiare);
    }

    private void validateUniqueName(String name) {
        if (getCaripiares().stream().anyMatch(caripiare -> caripiare.getName().equals(name)))
            throw new CaripiareException(HttpStatus.BAD_REQUEST, new CaripiareError(CaripiareErrorCode.CODE_01, CaripiareErrorCode.CODE_01.getMessage()));
    }

    private void validateParentsGender(UUID fatherId, UUID motherId) {
        if (fatherId != null) {
            Optional<Caripiare> optionalCaripiareFather = Optional.ofNullable(getCaripiare(fatherId));
            if (optionalCaripiareFather.isPresent() && !optionalCaripiareFather.get().getGender().matches(CaripiareDTO.MALE_GENDER_REGEX))
                throw new CaripiareException(HttpStatus.BAD_REQUEST, new CaripiareError(CaripiareErrorCode.CODE_03, CaripiareErrorCode.CODE_03.getMessage()));
        }
        if (motherId != null) {
            Optional<Caripiare> optionalCaripiareMother = Optional.ofNullable(getCaripiare(motherId));
            if (optionalCaripiareMother.isPresent() && !optionalCaripiareMother.get().getGender().matches(CaripiareDTO.FEMALE_GENDER_REGEX)) {
                throw new CaripiareException(HttpStatus.BAD_REQUEST, new CaripiareError(CaripiareErrorCode.CODE_03, CaripiareErrorCode.CODE_03.getMessage()));
            }
        }
    }

    @Override
    public Caripiare getCaripiare(UUID id) {
       return caripiareRepository.findById(id).orElseThrow(() ->
               new CaripiareException(HttpStatus.BAD_REQUEST, new CaripiareError(CaripiareErrorCode.CODE_02, CaripiareErrorCode.CODE_02.getMessage())));
    }

    @Override
    public CaripiareAndParentsDTO getCaripiareAndParents(String name) {
        Optional<Caripiare> optionalCaripiare = searchByUniqueName(name);
        if (optionalCaripiare.isEmpty()) return null;
        Caripiare father = Optional.ofNullable(optionalCaripiare.get().getFatherId()).map(this::getCaripiare).orElse(null);
        Caripiare mother = Optional.ofNullable(optionalCaripiare.get().getMotherId()).map(this::getCaripiare).orElse(null);
        return caripiareMapper.fromCaripiareDTOtoCaripiareAndParentsDTO(optionalCaripiare.get(), father, mother);
    }

    private Optional<Caripiare> searchByUniqueName(String name){
        return getCaripiares().stream().filter(caripiare -> name.equals(caripiare.getName())).findAny();
    }

    @Override
    public List<Caripiare> getCaripiares() {
        return StreamSupport.stream(caripiareRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Caripiare updateCaripiare(Caripiare caripiare) {
        Optional<Caripiare> optionalCaripiare = caripiareRepository.findById(caripiare.getId());
        if(optionalCaripiare.isPresent())
            return caripiareRepository.save(caripiare);
        throw new CaripiareException(HttpStatus.NOT_FOUND, new CaripiareError(CaripiareErrorCode.CODE_02, CaripiareErrorCode.CODE_02.getMessage()));
    }
}
