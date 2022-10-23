package com.co.edu.icesi.zooWeb.service.impl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode;
import com.co.edu.icesi.zooWeb.constants.BlackSwanStandards;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanError;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanException;
import com.co.edu.icesi.zooWeb.model.BlackSwan;
import com.co.edu.icesi.zooWeb.repository.BlackSwanRepository;
import com.co.edu.icesi.zooWeb.service.BlackSwanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode.*;

@AllArgsConstructor
@Service
public class BlackSwanServiceImpl implements BlackSwanService {

    public final BlackSwanRepository blackSwanRepository;

    @Override
    public List<BlackSwan> getSwan(String swanName) {
        BlackSwan temp = swanByName(swanName);
        if(temp != null){
            return swansList(temp);
        }
        throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_01,CODE_01.getMessage()));
    }
    private BlackSwan swanByName(String swanName) {
        return getSwans().stream().peek(System.out::println).
                filter(blackSwan -> blackSwan.getName().equalsIgnoreCase(swanName)).findFirst().orElse(null);
    }

    @Override
    public BlackSwan createSwan(BlackSwan blackSwan) {
        swansNameNotRepeated(blackSwan.getName());
        swansStandards(blackSwan);
        return blackSwanRepository.save(blackSwan);
    }

    @Override
    public List<BlackSwan> getSwans() {
        return StreamSupport.stream(blackSwanRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public BlackSwan updateSwan(String swanName, BlackSwan blackSwan) {
        BlackSwan currentSwan = swanByName(swanName);
        if(currentSwan != null){
            verifySwan(currentSwan,blackSwan);
            return blackSwanRepository.save(blackSwan);
        }
        throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_01,CODE_01.getMessage()));
    }

    private void verifySwan(BlackSwan currentSwan,BlackSwan newSwan){
        verifyNotNullAttributes(currentSwan,newSwan);
        validateSwansParents(newSwan.getFatherId(),newSwan.getMotherId());
        validateSex(currentSwan,newSwan);
        newSwan.setId(currentSwan.getId());
    }

    private void verifyNotNullAttributes(BlackSwan currentSwan, BlackSwan newSwan){
        if(newSwan.getMotherId()==null){
            newSwan.setMotherId(currentSwan.getMotherId());
        }
        if(newSwan.getFatherId()==null){
            newSwan.setFatherId(currentSwan.getFatherId());
        }
        if(newSwan.getName()==null){
            newSwan.setName(currentSwan.getName());
        }
        if(newSwan.getSex()==' '){
            newSwan.setSex(currentSwan.getSex());
        }
        if(newSwan.getArrivedZooDate()==null){
            newSwan.setArrivedZooDate(currentSwan.getArrivedZooDate());
        }
    }
    private void validateSex(BlackSwan currentSwan,BlackSwan newSwan){
        if(currentSwan.getSex() != newSwan.getSex()){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_13,CODE_13.getMessage()));
        }
    }

    private BlackSwan getSwanById(UUID blackSwanId){
        if(blackSwanId != null){
            return blackSwanRepository.findById(blackSwanId).orElse(null);
        }
        return  null;
    }

    //Done
    private void swansNameNotRepeated(String name) {
        List<BlackSwan> blackSwanList = getSwans();
        for (BlackSwan swan: blackSwanList) {
            if(swan.getName().equalsIgnoreCase(name)){
                throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                        new BlackSwanError(CODE_08,
                                CODE_08.getMessage()));
            }
        }
    }


    private void validateSwansParents(UUID fatherId, UUID motherId) {
        BlackSwan blackSwanFather = getSwanById(fatherId);
        BlackSwan blackSwanMother = getSwanById(motherId);
        if(blackSwanFather != null && blackSwanMother != null){
            validateParentsExist(blackSwanFather, blackSwanMother);
            validateSwansParentsSex(blackSwanFather, blackSwanMother);
        }
    }

    private void validateSwansParentsSex(BlackSwan blackSwanFather, BlackSwan blackSwanMother) {
        if(blackSwanFather.getSex()==(blackSwanMother.getSex())){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_05,
                            BlackSwanErrorCode.CODE_05.getMessage()));
        }
    }

    private void validateParentsExist(BlackSwan blackSwanFather, BlackSwan blackSwanMother) {
        if(blackSwanFather == null || blackSwanMother == null){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_10,
                            BlackSwanErrorCode.CODE_10.getMessage()));
        }
    }

    private void validateSwansHeight(double height, char sex) {

        double maxHeight = (sex==('F')) ? BlackSwanStandards.MALE_MAX_HEIGHT_CM : BlackSwanStandards.FEMALE_MAX_HEIGHT_CM;
        double minHeight = (sex==('M')) ? BlackSwanStandards.MALE_MIN_HEIGHT_CM : BlackSwanStandards.FEMALE_MIN_HEIGHT_CM;
        if(height > maxHeight || height < minHeight){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_03,
                            BlackSwanErrorCode.CODE_03.getMessage()));
        }
    }

    private void validateSwansAge(int age, char sex) {
        double maxAge = (sex==('F')) ? BlackSwanStandards.MALE_MAX_AGE : BlackSwanStandards.FEMALE_MAX_AGE;
        double minAge = (sex==('M')) ? BlackSwanStandards.MALE_MIN_AGE : BlackSwanStandards.FEMALE_MIN_AGE;
        if(age > maxAge || age < minAge){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_04,
                            BlackSwanErrorCode.CODE_04.getMessage()));
        }
    }

    private void validateSwansWeight(double weight, char sex) {
        double maxWeight = (sex==('F')) ? BlackSwanStandards.MALE_MAX_WEIGHT_KG : BlackSwanStandards.FEMALE_MAX_WEIGHT_KG;
        double minWeight = (sex==('M')) ? BlackSwanStandards.MALE_MIN_WEIGHT_KG : BlackSwanStandards.FEMALE_MIN_WEIGHT_KG;

        if(weight > maxWeight || weight < minWeight){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_06,
                            BlackSwanErrorCode.CODE_06.getMessage()));
        }
    }

    //Done
    private void swansStandards(BlackSwan blackSwan) {
        validateSwansWeight(blackSwan.getWeight(), blackSwan.getSex());
        validateSwansAge(blackSwan.getAge(), blackSwan.getSex());
        validateSwansHeight(blackSwan.getHeight(), blackSwan.getSex());
        validateSwansParents(blackSwan.getFatherId(), blackSwan.getMotherId());
    }

    private List<BlackSwan> swansList(BlackSwan blackSwan){
        List<BlackSwan> swans = new ArrayList<>();
        swans.add(blackSwan);
        swans.add(swanById(blackSwan.getFatherId(),false));
        swans.add(swanById(blackSwan.getMotherId(),true));
        return swans;
    }

    private BlackSwan swanById(UUID id,boolean sex){
        BlackSwan newSwan;
        if(sex){
            newSwan = new BlackSwan(UUID.nameUUIDFromBytes("CreatingAFemaleID".getBytes()),
                    "RandomName",'F',0,0,0,
                    LocalDateTime.of(2020,9,22,12,14,13)
                    ,UUID.nameUUIDFromBytes("CreatingAMaleID".getBytes()),
                    UUID.nameUUIDFromBytes("CreatingAFemaleID".getBytes()));
        }
        else{
            newSwan = new BlackSwan(UUID.nameUUIDFromBytes("CreatingAMaleID".getBytes()),
                    "RandomName",'F',0,0,0,
                    LocalDateTime.of(2020,9,22,12,14,13)
                    ,UUID.nameUUIDFromBytes("CreatingAMaleID".getBytes()),
                    UUID.nameUUIDFromBytes("CreatingAFemaleID".getBytes()));
        }
        return blackSwanRepository.findById(id).orElse(newSwan);
    }


}

