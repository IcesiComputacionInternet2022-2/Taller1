package co.edu.icesi.restzoo.controller;

import co.edu.icesi.restzoo.api.AnimalAPI;
import co.edu.icesi.restzoo.constant.AnimalErrorCode;
import co.edu.icesi.restzoo.constant.Constants;
import co.edu.icesi.restzoo.dto.AnimalDTO;
import co.edu.icesi.restzoo.error.exception.AnimalError;
import co.edu.icesi.restzoo.error.exception.AnimalException;
import co.edu.icesi.restzoo.mapper.AnimalMapper;
import co.edu.icesi.restzoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AnimalController implements AnimalAPI {

    public final AnimalMapper animalMapper;

    public final AnimalService animalService;

    public final int NAME_LENGTH_CAP = 120;

    @Override
    public AnimalDTO getAnimalById(UUID animalId) {
        return animalMapper.fromAnimal(animalService.getAnimal(animalId));
    }

    @Override
    public AnimalDTO getAnimalByName(String animalName) {
        return animalMapper.fromAnimal(animalService.getAnimal(animalName));
    }

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {
        validateAnimal(animalDTO);
        return animalMapper.fromAnimal(animalService.createAnimal(animalMapper.fromDTO(animalDTO)));
    }

    @Override
    public List<AnimalDTO> getAnimals() {
        return animalService.getAnimals().stream().map(animalMapper::fromAnimal).collect(Collectors.toList());
    }

    private void validateAnimal(AnimalDTO animalDTO) {
        validNameLength(animalDTO.getName());
        validNameFormat(animalDTO.getName());
        validSex(animalDTO.getSex());
        validAge(animalDTO.getAge());
        healthyWeightFloor(animalDTO.getWeight());
        healthyWeightCeil(animalDTO.getWeight());
        babyLengthFloor(animalDTO.getLength());
        elderLengthCeil(animalDTO.getLength());
    }

    private void validNameFormat(String name) {
        String VALID_NAME_FORMAT = "^[a-zA-Z\\s]*$";
        if (!name.matches(VALID_NAME_FORMAT))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x11, AnimalErrorCode.CRL_E0x11.getMessage()));
    }

    private void validNameLength(String name) {
        if (name.length() > NAME_LENGTH_CAP)
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x12, AnimalErrorCode.CRL_E0x12.getMessage()));
    }

    private void validSex(char sex) {
        if (!Character.toString(sex).matches("^[FfmM]$"))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x13, AnimalErrorCode.CRL_E0x13.getMessage()));
    }

    private void validAge(double age) {
        if (age > Double.parseDouble(Constants.MAX_LONGEVITY.getValue()) || age < 0)
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x14, AnimalErrorCode.CRL_E0x14.getMessage()));
    }

    private void healthyWeightFloor(double weight) {
        if (weight < Double.parseDouble(Constants.MIN_HEALTHY_WEIGHT.getValue()))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x15_1, AnimalErrorCode.CRL_E0x15_1.getMessage()));
    }

    private void healthyWeightCeil(double weight) {
        if (weight > Double.parseDouble(Constants.MAX_HEALTHY_WEIGHT.getValue()))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x15_2, AnimalErrorCode.CRL_E0x15_2.getMessage()));
    }

    private void babyLengthFloor(double length) {
        if (length < Double.parseDouble(Constants.MIN_BABY_LENGTH.getValue()))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x16_1, AnimalErrorCode.CRL_E0x16_1.getMessage()));
    }

    private void elderLengthCeil(double length) {
        if (length > Double.parseDouble(Constants.MAX_ELDER_LENGTH.getValue()))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.CRL_E0x16_2, AnimalErrorCode.CRL_E0x16_2.getMessage()));
    }
}
