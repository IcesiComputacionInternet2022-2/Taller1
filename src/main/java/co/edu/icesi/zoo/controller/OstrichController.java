package co.edu.icesi.zoo.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.icesi.zoo.api.OstrichAPI;
import co.edu.icesi.zoo.constant.OstrichConstant;
import co.edu.icesi.zoo.constant.OstrichErrorCode;
import co.edu.icesi.zoo.dto.OstrichDTO;
import co.edu.icesi.zoo.error.exception.OstrichError;
import co.edu.icesi.zoo.error.exception.OstrichException;
import co.edu.icesi.zoo.mapper.OstrichMapper;
import co.edu.icesi.zoo.service.OstrichService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OstrichController implements OstrichAPI {

    private final OstrichMapper ostrichMapper;

    private final OstrichService ostrichService;

    @Override
    public OstrichDTO createOstrich(OstrichDTO ostrichDTO) {
    	nameLengthMustBeLessThan120Characteres(ostrichDTO);
    	nameShouldOnlyContainLettersAndSpaces(ostrichDTO);
    	arrivalDateCannotBeLaterThanTheCurrentDate(ostrichDTO);
    	validateWeight(ostrichDTO);
    	validateAge(ostrichDTO);
    	validateHeight(ostrichDTO);
    	validateGender(ostrichDTO);
        return ostrichMapper.fromOstrich(ostrichService.createOstrich(ostrichMapper.fromDTO(ostrichDTO)));
    }
    
    private void nameLengthMustBeLessThan120Characteres(OstrichDTO ostrichDTO) {
    	if(ostrichDTO.getName().length() > 120) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_02.name(), OstrichErrorCode.CODE_02.getMessage()));
    	}
    }

	private void nameShouldOnlyContainLettersAndSpaces(OstrichDTO ostrichDTO) {
    	String name = ostrichDTO.getName().replaceAll(" ", "");
    	if(!name.matches("^[a-zA-Z]*$")) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_03.name(), OstrichErrorCode.CODE_03.getMessage()));
    	}
    }

	private void arrivalDateCannotBeLaterThanTheCurrentDate(OstrichDTO ostrichDTO) {
    	if(LocalDateTime.now().isBefore(ostrichDTO.getArrivalDate())) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_04.name(), OstrichErrorCode.CODE_04.getMessage()));
    	}
	}
    
    private void validateWeight(OstrichDTO ostrichDTO) {
    	if(ostrichDTO.getWeight() < OstrichConstant.WEIGHT.getMin() || ostrichDTO.getWeight() > OstrichConstant.WEIGHT.getMax()) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_05.name(), OstrichErrorCode.CODE_05.getMessage()));
    	}
    }
    
    private void validateAge(OstrichDTO ostrichDTO) {
    	if(ostrichDTO.getAge() < OstrichConstant.AGE.getMin() || ostrichDTO.getAge() > OstrichConstant.AGE.getMax()) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_06.name(), OstrichErrorCode.CODE_06.getMessage()));
    	}
    }
    
    private void validateHeight(OstrichDTO ostrichDTO) {
    	if(ostrichDTO.getHeight() < OstrichConstant.HEIGHT.getMin() || ostrichDTO.getHeight() > OstrichConstant.HEIGHT.getMax()) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_07.name(), OstrichErrorCode.CODE_07.getMessage()));
    	}
    }
    
    private void validateGender(OstrichDTO ostrichDTO) {
    	if(ostrichDTO.getGender() != OstrichConstant.GENDER.getMin() && ostrichDTO.getGender() != OstrichConstant.GENDER.getMax()) {
    		throw new OstrichException(HttpStatus.BAD_REQUEST, new OstrichError(OstrichErrorCode.CODE_08.name(), OstrichErrorCode.CODE_08.getMessage()));
    	}
    }
    
    @Override
    public OstrichDTO getOstrichById(UUID ostrichId) {
        return ostrichMapper.fromOstrich(ostrichService.getOstrichById(ostrichId));
    }
    
    @Override
    public List<OstrichDTO> getOstrichByName(String ostrichName) {
    	return ostrichService.getOstrichByName(ostrichName).stream().map(ostrichMapper::fromOstrich).collect(Collectors.toList());
    }

    @Override
    public List<OstrichDTO> getOstriches() {
        return ostrichService.getOstriches().stream().map(ostrichMapper::fromOstrich).collect(Collectors.toList());
    }

}
