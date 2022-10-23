package com.edu.icesi.CaliZoo.service.impl;

import com.edu.icesi.CaliZoo.constants.ErrorCodes;
import com.edu.icesi.CaliZoo.error.exception.ToucanError;
import com.edu.icesi.CaliZoo.error.exception.ToucanException;
import com.edu.icesi.CaliZoo.model.Toucan;
import com.edu.icesi.CaliZoo.repository.ToucanRepository;
import com.edu.icesi.CaliZoo.service.ToucanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ToucanServiceImpl implements ToucanService {

    public final ToucanRepository toucanRepository;

    @Override
    public List<Toucan> getToucan(String toucanName) {
        try{
            Toucan ChosenToucan = getToucans().stream().filter(toucan->toucan.getName().equalsIgnoreCase(toucanName))
                    .findFirst().get();
            return getToucanParents(ChosenToucan);
        }catch (NoSuchElementException e){
            throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.NOT_FOUND.getCode(), "There is not a toucan with the name "+toucanName));
        }//End try..catch
    }//End getToucan

    @Override
    public Toucan createToucan(Toucan toucanDTO) {
        if(toucanDTO == null)
            throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.BAD_DATA.getCode(), "Null data is not valid to create a Toucan"));
        thereIsToucanWithName(toucanDTO.getName());
        validateParentSex(toucanDTO.getMotherId(),"F");
        validateParentSex(toucanDTO.getFatherId(),"M");
        thereIsParent(toucanDTO.getFatherId());
        thereIsParent(toucanDTO.getMotherId());
        return toucanRepository.save(toucanDTO);
    }//End createToucan

    @Override
    public List<Toucan> getToucans() {
        return StreamSupport.stream(toucanRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    private void thereIsToucanWithName(final String toucanName){
        if(getToucans().stream().filter(a -> a.getName().equalsIgnoreCase(toucanName)).findFirst().orElse(null) != null){
            throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.BAD_DATA.getCode(), "There is already a Toucan with that name"));
        }
    }//End thereIsToucanWithName

    private void validateParentSex(UUID parentId, String sex){
        if(parentId != null){
            Optional<Toucan> parent = toucanRepository.findById(parentId);
            if(parent.isPresent() && !parent.get().getSex().toUpperCase().matches(sex))
                throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.BAD_DATA.getCode(), "Invalid parent sex"));
        }//End if
    }//End validateParentsSex

    private List<Toucan> getToucanParents(Toucan toucan){
        List<Toucan> toucanAndParents = new ArrayList<>();
        toucanAndParents.add(toucan);
        UUID fatherId = toucan.getFatherId();
        UUID motherId = toucan.getMotherId();
        if(fatherId != null ) toucanAndParents.add(toucanRepository.findById(fatherId).orElse(null));
        if(motherId != null ) toucanAndParents.add(toucanRepository.findById(motherId).orElse(null));
        return toucanAndParents;
    }//End getToucanParents

    private void thereIsParent(UUID parentId){
        if(parentId != null && toucanRepository.findById(parentId).orElse(null) == null)
            throw new ToucanException(HttpStatus.BAD_REQUEST, new ToucanError(ErrorCodes.BAD_DATA.getCode(), "There is not a parent with that ID"));
    }//End thereIsParent
}//End ToucanServiceImpl
