package co.edu.icesi.zoo.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.edu.icesi.zoo.constant.OstrichConstant;
import co.edu.icesi.zoo.constant.OstrichErrorCode;
import co.edu.icesi.zoo.error.exception.OstrichError;
import co.edu.icesi.zoo.error.exception.OstrichException;
import co.edu.icesi.zoo.model.Ostrich;
import co.edu.icesi.zoo.repository.OstrichRepository;
import co.edu.icesi.zoo.service.OstrichService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OstrichServiceImpl implements OstrichService {

    private final OstrichRepository ostrichRepository;

    @Override
    public Ostrich createOstrich(Ostrich ostrich) {
    	uniqueNames(ostrich);
    	validateFather(ostrich);
    	validateMother(ostrich);
    	validateFatherGenre(ostrich);
    	validateMotherGenre(ostrich);
        return ostrichRepository.save(ostrich);
    }

	private void uniqueNames(Ostrich ostrich) {
    	if(ostrichRepository.findByName(ostrich.getName()).isPresent()) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_01.name(), OstrichErrorCode.CODE_01.getMessage()));
    	}
    }
    
    private void validateFather(Ostrich ostrich) {
		if(ostrich.getFatherId() != null && ostrichRepository.findById(ostrich.getFatherId()).isEmpty()) {
			throw new OstrichException(HttpStatus.NOT_FOUND, new OstrichError(OstrichErrorCode.CODE_09.name(), OstrichErrorCode.CODE_09.getMessage()));
		}
	}
    
    private void validateMother(Ostrich ostrich) {
    	if(ostrich.getMotherId() != null && ostrichRepository.findById(ostrich.getMotherId()).isEmpty()) {
			throw new OstrichException(HttpStatus.NOT_FOUND, new OstrichError(OstrichErrorCode.CODE_10.name(), OstrichErrorCode.CODE_10.getMessage()));
		}
	}

	private void validateFatherGenre(Ostrich ostrich) {
		if (ostrich.getFatherId() != null && ostrichRepository.findById(ostrich.getFatherId()).isPresent() && ostrichRepository.findById(ostrich.getFatherId()).get().getGender() != OstrichConstant.GENDER.getMax()) {
			throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_11.name(), OstrichErrorCode.CODE_11.getMessage()));
		}
	}
	
	private void validateMotherGenre(Ostrich ostrich) {
		if (ostrich.getMotherId() != null && ostrichRepository.findById(ostrich.getMotherId()).isPresent() && ostrichRepository.findById(ostrich.getMotherId()).get().getGender() != OstrichConstant.GENDER.getMin()) {
			throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_12.name(), OstrichErrorCode.CODE_12.getMessage()));
		}
	}

    @Override
    public Ostrich getOstrichById(UUID ostrichId) {
    	return ostrichRepository.findById(ostrichId).orElseThrow(() -> new OstrichException(HttpStatus.NOT_FOUND, new OstrichError(OstrichErrorCode.CODE_13.name(), OstrichErrorCode.CODE_13.getMessage())));
    }
    
    @Override
    public List<Ostrich> getOstrichByName(String ostrichName) {
    	List<Ostrich> ostriches = new ArrayList<>();
    	ostriches.add(ostrichRepository.findByName(ostrichName).orElseThrow(() -> new OstrichException(HttpStatus.NOT_FOUND, new OstrichError(OstrichErrorCode.CODE_13.name(), OstrichErrorCode.CODE_13.getMessage()))));
    	if(ostriches.get(0).getFatherId() != null) {
			ostriches.add(ostrichRepository.findById(ostriches.get(0).getFatherId()).orElseThrow(() -> new OstrichException(HttpStatus.NOT_FOUND, new OstrichError(OstrichErrorCode.CODE_09.name(), OstrichErrorCode.CODE_09.getMessage()))));
		}
		if(ostriches.get(0).getMotherId() != null) {
			ostriches.add(ostrichRepository.findById(ostriches.get(0).getMotherId()).orElseThrow(() -> new OstrichException(HttpStatus.NOT_FOUND, new OstrichError(OstrichErrorCode.CODE_10.name(), OstrichErrorCode.CODE_10.getMessage()))));
		}
        return ostriches;
    }

    @Override
    public List<Ostrich> getOstriches() {
        return StreamSupport.stream(ostrichRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }
    
}
