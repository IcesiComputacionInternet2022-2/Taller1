package com.co.edu.icesi.zooWeb.controller;

import com.co.edu.icesi.zooWeb.api.BlackSwanAPI;
import com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode;
import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanError;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanException;
import com.co.edu.icesi.zooWeb.mapper.BlackSwanMapper;
import com.co.edu.icesi.zooWeb.service.BlackSwanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode.*;
import static com.co.edu.icesi.zooWeb.constants.BlackSwanStandards.*;

@RestController
@AllArgsConstructor
public class BlackSwanController implements BlackSwanAPI {


    private BlackSwanService blackSwanService;
    private BlackSwanMapper blackSwanMapper;


    @Override
    public List<BlackSwanDTO> getSwan(String swanName) {
        return blackSwanService.getSwan(swanName).stream().map(blackSwanMapper::fromBlackSwan).collect(Collectors.toList());
    }

    @Override
    public BlackSwanDTO createSwan(BlackSwanDTO blackSwanDTO) {
        validateBlackSwan(blackSwanDTO);
        return blackSwanMapper.fromBlackSwan(blackSwanService.createSwan(blackSwanMapper.fromBlackSwanDTO(blackSwanDTO)));
    }

    @Override
    public List<BlackSwanDTO> getSwans() {
        return blackSwanService.getSwans().stream().map(blackSwanMapper::fromBlackSwan).collect(Collectors.toList());
    }

    @Override
    public BlackSwanDTO updateSwan(String swanName, BlackSwanDTO blackSwanDTO) {
        validateBlackSwan(blackSwanDTO);
        validateBlackSwanArrivedDate(blackSwanDTO.getArrivedZooDate());
        validateBlackSwanHeight(blackSwanDTO.getHeight(),blackSwanDTO.getSex());
        validateBlackSwanWeight(blackSwanDTO.getWeight(),blackSwanDTO.getSex());
        validateAge(blackSwanDTO.getAge());
        return blackSwanMapper.fromBlackSwan(blackSwanService.updateSwan
                (swanName,blackSwanMapper.fromDTO(swanName, blackSwanDTO)));
    }

    private void validateAge(int age){
        if (!(MALE_MIN_AGE <= age && age <= MALE_MAX_AGE)) {
            throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_04, CODE_04.getMessage()));
        }
    }
    private void validateBlackSwanWeight(double weight, char sex){
        if(sex=='F'){
            if (!(FEMALE_MIN_WEIGHT_KG <= weight && weight <= FEMALE_MAX_WEIGHT_KG)) {
                throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_06, CODE_06.getMessage()));
            }
        }
        else{
            if (!(MALE_MIN_WEIGHT_KG <= weight && weight <= MALE_MAX_WEIGHT_KG)) {
                throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_06, CODE_06.getMessage()));
            }
        }
    }
    private void validateBlackSwanHeight(double height, char sex){
        if(sex=='F'){
            if (!(FEMALE_MIN_HEIGHT_CM <= height && height <= FEMALE_MAX_HEIGHT_CM)) {
                throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_03, CODE_03.getMessage()));
            }
        }
        else{
            if (!(MALE_MIN_HEIGHT_CM <= height && height <= MALE_MAX_HEIGHT_CM)) {
                throw new BlackSwanException(HttpStatus.BAD_REQUEST, new BlackSwanError(CODE_03, CODE_03.getMessage()));
            }
        }
    }

    private void validateBlackSwanArrivedDate(LocalDateTime arrivedZooDate) {
        if(arrivedZooDate.isAfter(LocalDateTime.now())){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_13,
                            BlackSwanErrorCode.CODE_13.getMessage()));
        }
    }

    private void validateBlackSwanName(String swanName) {
        validateBlackSwanNameLength(swanName);
        validateBlackSwanNameValues(swanName);
    }
    private void validateBlackSwanNameLength(String swanName) {
        if(swanName.length() > 120){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_07,
                            BlackSwanErrorCode.CODE_07.getMessage()));
        }
    }
    private void validateBlackSwanNameValues(String swanName) {
        if(!swanName.matches("[\\sa-zA-Z]+")){
            throw new BlackSwanException(HttpStatus.BAD_REQUEST,
                    new BlackSwanError(BlackSwanErrorCode.CODE_02,
                            BlackSwanErrorCode.CODE_02.getMessage()));
        }
    }
    private void validateBlackSwan(BlackSwanDTO blackSwanDTO) {
        validateBlackSwanName(blackSwanDTO.getName());
        validateBlackSwanArrivedDate(blackSwanDTO.getArrivedZooDate());
    }


}

