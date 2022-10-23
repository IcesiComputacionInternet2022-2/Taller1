package co.edu.icesi.zoocanyonriver.service.impl;

import co.edu.icesi.zoocanyonriver.constants.CodesError;
import co.edu.icesi.zoocanyonriver.constants.Genders;
import co.edu.icesi.zoocanyonriver.constants.TigerCharacteristics;
import co.edu.icesi.zoocanyonriver.dto.TigerResponseDTO;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoError;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoException;
import co.edu.icesi.zoocanyonriver.mapper.TigerMapper;
import co.edu.icesi.zoocanyonriver.model.Tiger;
import co.edu.icesi.zoocanyonriver.repository.TigerRepository;
import co.edu.icesi.zoocanyonriver.service.TigerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Macho:
 * -Peso:90-310kg
 * -edad:26 años
 * -estatura:80-110cm
 * Hembra:
 * -Peso:65-170kg
 * -edad:26 años
 * -estaturas:80-110cm
 */
@AllArgsConstructor
@Service
public class TigerServiceImpl implements TigerService {

    private final TigerRepository tigerRepository;

    private final TigerMapper tigerMapper;

    @Override
    public TigerResponseDTO getTiger(String tigerName) {
        return searchByName(tigerName, StreamSupport.stream(tigerRepository.findAll().spliterator(), false).collect(Collectors.toList()));
    }

    @Override
    public Tiger getTiger(UUID tigerId) {
        Optional<Tiger> tiger = tigerRepository.findById(tigerId);
        return tiger.orElseThrow(() -> new TigerDemoException(HttpStatus.BAD_REQUEST,
                new TigerDemoError(CodesError.CODE_04.getCode(), CodesError.CODE_04.getMessage())));
    }

    private TigerResponseDTO searchByName(String tigerName, List<Tiger> tigers) {
        for (Tiger tiger : tigers) {
            if (tiger.getName().equalsIgnoreCase(tigerName)) {
                return getTigerDTOParents(tiger);
            }
        }
        throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_03.getCode(), CodesError.CODE_03.getMessage()));
    }

    private TigerResponseDTO getTigerDTOParents(Tiger tiger) {
        Tiger mother = (tiger.getMother()!=null)?getTiger(UUID.fromString(tiger.getMother())):null;
        Tiger father = (tiger.getFather()!=null)?getTiger(UUID.fromString(tiger.getFather())):null;
        TigerResponseDTO tigerResponseDTO = tigerMapper.fromTigerToTigerResponseDTO(tiger, mother, father);

        return tigerResponseDTO;
    }

    @Override
    public Tiger createTiger(Tiger tiger) {
        verifyExistenceParents(tiger);
        verifyNameRepeat(tiger);
        verifyWeightGender(tiger);
        verifyAge(tiger.getAge());
        verifyHeight(tiger.getHeight());
        verifyDate(tiger.getArriveDate());

        return tigerRepository.save(tiger);
    }

    private void verifyNameRepeat(Tiger tiger) {
        List<Tiger> tigers = StreamSupport.stream(tigerRepository.findAll().spliterator(), false).collect(Collectors.toList());
        for (Tiger currentTiger : tigers) {
            if (currentTiger.getName().equalsIgnoreCase(tiger.getName())) {
                throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_12.getCode(), CodesError.CODE_12.getMessage()));
            }
        }
    }

    private void verifyExistenceParents(Tiger tiger) {
        if (tiger.getMother() != null) {
            verifyMother(UUID.fromString(tiger.getMother()));
        }
        if (tiger.getFather() != null) {
            verifyFather(UUID.fromString(tiger.getFather()));
        }
    }

    private void verifyMother(UUID uuidMother) {
        Optional<Tiger> mother = tigerRepository.findById(uuidMother);
        if (!mother.isPresent()) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_13.getCode(), CodesError.CODE_13.getMessage()));
        } else {
            verifyGenderMother(mother.get().getGender());
        }
    }

    private void verifyGenderMother(String gender) {
        if (!gender.equalsIgnoreCase(Genders.FEMALE.getValue())) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_15.getCode(), CodesError.CODE_15.getMessage()));
        }
    }

    private void verifyFather(UUID uuidFather) {
        Optional<Tiger> father = tigerRepository.findById(uuidFather);
        if (!father.isPresent()) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_14.getCode(), CodesError.CODE_14.getMessage()));
        } else {
            verifyGenderFather(father.get().getGender());
        }
    }

    private void verifyGenderFather(String gender) {
        if (!gender.equalsIgnoreCase(Genders.MALE.getValue())) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_16.getCode(), CodesError.CODE_16.getMessage()));
        }
    }


    private void verifyHeight(String height) {
        if (Integer.valueOf(height) > TigerCharacteristics.MAX_HEIGHT || Integer.valueOf(height) < TigerCharacteristics.MIN_HEIGHT) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_09.getCode(), CodesError.CODE_09.getMessage()));
        }
    }

    private void verifyAge(String age) {
        if (Integer.parseInt(age) > TigerCharacteristics.MAX_AGE || Integer.parseInt(age) < TigerCharacteristics.MIN_AGE) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_08.getCode(), CodesError.CODE_08.getMessage()));
        }
    }

    private void verifyWeightGender(Tiger tiger) {
        if (tiger.getGender().equalsIgnoreCase(Genders.MALE.getValue())) {
            verifyWeightMale(tiger.getWeight());
        } else if (tiger.getGender().equalsIgnoreCase(Genders.FEMALE.getValue())) {
            verifyWeightFemale(tiger.getWeight());
        } else {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_07.getCode(), CodesError.CODE_07.getMessage()));
        }
    }

    private void verifyWeightMale(double weight) {
        if (weight > TigerCharacteristics.MALE_MAX_WEIGHT || weight < TigerCharacteristics.MALE_MIN_WEIGHT) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_05.getCode(), CodesError.CODE_05.getMessage()));
        }
    }

    private void verifyDate(LocalDateTime arriveDate) {
        if (arriveDate.isAfter(LocalDateTime.now())) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_11.getCode(), CodesError.CODE_11.getMessage()));
        }
    }

    private void verifyWeightFemale(double weight) {
        if (weight > TigerCharacteristics.FEMALE_MAX_WEIGHT || weight < TigerCharacteristics.FEMALE_MIN_WEIGHT) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST, new TigerDemoError(CodesError.CODE_06.getCode(), CodesError.CODE_06.getMessage()));
        }
    }

    @Override
    public List<Tiger> getTigers() {
        return StreamSupport.stream(tigerRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
