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
        validateParentSex(toucanDTO.getMotherName(),"F");
        validateParentSex(toucanDTO.getFatherName(),"M");
        thereIsParent(toucanDTO.getFatherName());
        thereIsParent(toucanDTO.getMotherName());
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

    private void validateParentSex(String parentName, String sex){
        if(parentName != null){
            Toucan parent = getParent(parentName);
            if(parent != null && !parent.getSex().toUpperCase().matches(sex))
                throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.BAD_DATA.getCode(), "Invalid parent sex"));
        }//End if
    }//End validateParentsSex

    private List<Toucan> getToucanParents(Toucan toucan){
        List<Toucan> toucanAndParents = new ArrayList<>();
        toucanAndParents.add(toucan);
        Toucan father = getParent(toucan.getFatherName());
        Toucan mother = getParent(toucan.getMotherName());
        if(father != null)
            toucanAndParents.add(father);
        if(mother != null)
            toucanAndParents.add(mother);
        return toucanAndParents;
    }//End getToucanParents

    private Toucan getParent(String name){
        Toucan parent = null;
        if(name != null ){
            try{
                parent = getToucans().stream().filter(f->f.getName().equalsIgnoreCase(name)).findFirst().get();
            }catch (NoSuchElementException e){
                parent = null;
            }//End try..catch
        }
        return parent;
    }
    private void thereIsParent(String parentName){
        if(parentName != null && getParent(parentName) == null)
            throw new ToucanException(HttpStatus.BAD_REQUEST, new ToucanError(ErrorCodes.BAD_DATA.getCode(), "There is not a parent with that ID"));
    }//End thereIsParent
}//End ToucanServiceImpl
