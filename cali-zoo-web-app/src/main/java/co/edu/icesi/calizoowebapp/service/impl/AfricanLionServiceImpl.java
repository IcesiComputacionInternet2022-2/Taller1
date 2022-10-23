package co.edu.icesi.calizoowebapp.service.impl;

import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import co.edu.icesi.calizoowebapp.constants.AfricanLionStandards;
import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionError;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionException;
import co.edu.icesi.calizoowebapp.model.AfricanLion;
import co.edu.icesi.calizoowebapp.model.AfricanLionQueryResponse;
import co.edu.icesi.calizoowebapp.repository.AfricanLionRespository;
import co.edu.icesi.calizoowebapp.service.AfricanLionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class AfricanLionServiceImpl implements AfricanLionService {

    public final AfricanLionRespository africanLionRespository;

    @Override
    public AfricanLionQueryResponse getLion(String lionName) {
        Optional<AfricanLion> africanLion = getLionByName(lionName);
        if(africanLion.isPresent()){
            AfricanLion requestedLion = africanLion.get();
            AfricanLion lionFather = getLionById(africanLion.get().getFatherId());
            AfricanLion lionMother = getLionById(africanLion.get().getMotherId());
            return new AfricanLionQueryResponse(requestedLion, lionFather, lionMother);
        }
        throw new AfricanLionException(HttpStatus.NOT_FOUND, new AfricanLionError(AfricanLionErrorCode.CODE_01, AfricanLionErrorCode.CODE_01.getMessage()));
    }

    @Override
    public AfricanLion createLion(AfricanLion africanLion) {
        lionsNameIsUnique(africanLion.getName());
        areLionCharacteristicsInStandards(africanLion);
        validateLionsParents(africanLion.getFatherId(), africanLion.getMotherId());
        return africanLionRespository.save(africanLion);
    }

    @Override
    public List<AfricanLion> getLions() {
        return StreamSupport.stream(africanLionRespository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    private void lionsNameIsUnique(String name) {
        List<AfricanLion> africanLionList = getLions();
        for (AfricanLion lion: africanLionList) {
            if(lion.getName().equalsIgnoreCase(name)){
                throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_02, AfricanLionErrorCode.CODE_02.getMessage()));
            }
        }
    }

    private void areLionCharacteristicsInStandards(AfricanLion africanLion) {
        validateLionsWeight(africanLion.getWeight(), africanLion.getSex());
        validateLionsAge(africanLion.getAge(), africanLion.getSex());
        validateLionsHeight(africanLion.getHeight(), africanLion.getSex());
    }

    private void validateLionsParents(UUID fatherId, UUID motherId) {
        if(fatherId != null && motherId != null){
            AfricanLion africanLionFather = getLionById(fatherId);
            AfricanLion africanLionMother = getLionById(motherId);
            validateMotherAndFatherExists(africanLionFather, africanLionMother);
            if(africanLionFather != null && africanLionMother != null){
                validateLionsParentsSex(africanLionFather, africanLionMother);
            }
        }
    }

    private void validateLionsParentsSex(AfricanLion africanLionFather, AfricanLion africanLionMother) {
        if(africanLionFather.getSex().equals(africanLionMother.getSex())){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_03, AfricanLionErrorCode.CODE_03.getMessage()));
        }
    }

    private void validateMotherAndFatherExists(AfricanLion africanLionFather, AfricanLion africanLionMother) {
        List<AfricanLion> africanLions = getLions();
        if(!africanLions.contains(africanLionFather) || !africanLions.contains(africanLionMother)){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_10, AfricanLionErrorCode.CODE_10.getMessage()));
        }
    }

    private void validateLionsHeight(double height, AnimalSex sex) {

        double maxHeight = (sex.equals(AnimalSex.MALE)) ? AfricanLionStandards.MALE_MAX_HEIGHT_CM : AfricanLionStandards.FEMALE_MAX_HEIGHT_CM;
        double minHeight = (sex.equals(AnimalSex.MALE)) ? AfricanLionStandards.MALE_MIN_HEIGHT_CM : AfricanLionStandards.FEMALE_MIN_HEIGHT_CM;
        if(height > maxHeight || height < minHeight){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_04, AfricanLionErrorCode.CODE_04.getMessage()));
        }
    }

    private void validateLionsAge(int age, AnimalSex sex) {
        double maxAge = (sex.equals(AnimalSex.MALE)) ? AfricanLionStandards.MALE_MAX_AGE : AfricanLionStandards.FEMALE_MAX_AGE;
        double minAge = (sex.equals(AnimalSex.MALE)) ? AfricanLionStandards.MALE_MIN_AGE : AfricanLionStandards.FEMALE_MIN_AGE;
        if(age > maxAge || age < minAge){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_05, AfricanLionErrorCode.CODE_05.getMessage()));
        }
    }

    private void validateLionsWeight(double weight, AnimalSex sex) {
        double maxWeight = (sex.equals(AnimalSex.MALE)) ? AfricanLionStandards.MALE_MAX_WEIGHT_KG : AfricanLionStandards.FEMALE_MAX_WEIGHT_KG;
        double minWeight = (sex.equals(AnimalSex.MALE)) ? AfricanLionStandards.MALE_MIN_WEIGHT_KG : AfricanLionStandards.FEMALE_MIN_WEIGHT_KG;

        if(weight > maxWeight || weight < minWeight){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_06, AfricanLionErrorCode.CODE_06.getMessage()));
        }
    }

    private Optional<AfricanLion> getLionByName(String lionName) {
        List<AfricanLion> africanLionList = getLions();
        AfricanLion africanLion = null;
        for (AfricanLion temporalLion : africanLionList) {
            if (temporalLion.getName().equalsIgnoreCase(lionName)) {
                africanLion = temporalLion;
            }
        }

        return Optional.ofNullable(africanLion);
    }

    private AfricanLion getLionById(UUID africanLionId){
        if(africanLionId != null){
            return africanLionRespository.findById(africanLionId).orElse(null);
        }
        return  null;
    }
}
