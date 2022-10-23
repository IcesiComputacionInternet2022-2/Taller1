package co.edu.icesi.spring_zoo_cusumbo.service.impl;

import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboError;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboException;
import co.edu.icesi.spring_zoo_cusumbo.model.Cusumbo;
import co.edu.icesi.spring_zoo_cusumbo.repository.CusumboRepository;
import co.edu.icesi.spring_zoo_cusumbo.service.CusumboService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static co.edu.icesi.spring_zoo_cusumbo.error.ErrorCode.*;


@Service
@AllArgsConstructor
public class CusumboServiceImpl implements CusumboService {

    public final CusumboRepository cusumboRepository;

    @Override
    public List<Cusumbo> getCusumboWithParents(String cusumboName) {
        List<Cusumbo> cusumbosFamily = new ArrayList<>();

        Cusumbo cusumbo = getCusumboByNameAndVerifyExistence(cusumboName);
        cusumbosFamily.add(0,cusumbo);

        UUID fatherId = cusumbo.getFatherId();
        if(fatherId !=null) cusumbosFamily.add(1,getCusumboById(fatherId));

        UUID motherId = cusumbo.getMotherId();
        if(motherId !=null) cusumbosFamily.add(2,getCusumboById(motherId));

        return cusumbosFamily;
    }

    private Cusumbo getCusumboByNameAndVerifyExistence(String cusumboName) {
        Optional<Cusumbo> cusumbo = cusumboRepository.findByName(cusumboName);

        if(cusumbo.isEmpty())
            throw new CusumboException(HttpStatus.NOT_FOUND, new CusumboError(CODE_SEARCH_01.getMessage(),CODE_SEARCH_01));

        return cusumbo.get();
    }

    private Cusumbo getCusumboById(UUID cusumboId) {
        return cusumboRepository.findById(cusumboId).orElse(null);
    }

    @Override
    @SneakyThrows
    public Cusumbo createCusumbo(Cusumbo cusumbo) {

        validateUniqueName(cusumbo.getName());
        validateParents(cusumbo.getFatherId(),cusumbo.getMotherId());

        return cusumboRepository.save(cusumbo);
    }

    //Unique name and parents validation was done on the service because checks on the repository are needed
    private void validateParents(UUID fatherId, UUID motherId){

        if(fatherId != null && motherId != null){
            validateParentExistence(fatherId);
            validateParentExistence(motherId);
            validateParentsSex(fatherId,motherId);
        }

        if(fatherId != null) {
            validateParentExistence(fatherId);
            validateFatherSex(fatherId);
        }
        if(motherId != null){
            validateParentExistence(motherId);
            validateMotherSex(motherId);
        }
    }

    //Throws exception when name is taken
    private void validateUniqueName(String name){
        if(cusumboRepository.findByName(name).isPresent())
            throw new CusumboException(HttpStatus.CONFLICT, new CusumboError(CODE_ATR_01B.getMessage(),CODE_ATR_01B));
    }

    //Throws exception if parent does not exist
    private void validateParentExistence(UUID parentId){
        if(!cusumboRepository.findById(parentId).isPresent())
            throw new CusumboException(HttpStatus.NOT_FOUND, new CusumboError(CODE_ATR_07A.getMessage(),CODE_ATR_07A));
    }

    private void validateParentsSex(UUID fatherId, UUID motherId){
        if(getCusumboById(fatherId).getSex() == getCusumboById(motherId).getSex()) {
            throw new CusumboException(HttpStatus.CONFLICT, new CusumboError(CODE_ATR_07B.getMessage(), CODE_ATR_07B));
        }
        validateFatherSex(fatherId);
        validateMotherSex(motherId);
    }

    private void validateFatherSex(UUID fatherId){
        if(getCusumboById(fatherId).getSex() != 'M')
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_07C.getMessage(),CODE_ATR_07C));
    }

    private void validateMotherSex(UUID motherId){
        if(getCusumboById(motherId).getSex() != 'F')
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_07C.getMessage(),CODE_ATR_07C));
    }

    @Override
    public List<Cusumbo> getCusumbos() {
        return StreamSupport.stream(cusumboRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }
}
