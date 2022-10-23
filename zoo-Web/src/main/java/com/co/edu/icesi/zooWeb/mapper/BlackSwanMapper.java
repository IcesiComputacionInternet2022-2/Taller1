package com.co.edu.icesi.zooWeb.mapper;

import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import com.co.edu.icesi.zooWeb.model.BlackSwan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlackSwanMapper {

    BlackSwan fromBlackSwanDTO(BlackSwanDTO blackSwanDTO);
    BlackSwanDTO fromBlackSwan(BlackSwan blackSwan);
    BlackSwan fromDTO(String swanName, BlackSwanDTO blackSwanDTO);
}

