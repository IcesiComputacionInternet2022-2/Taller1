package com.icesi.edu.zoo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnimalErrorCode {

    //Used in Controller
    CODE_01("La peticion no puede estar vacia"),
    CODE_02("El nombre no es valido. Verifica que el tamanio es menor o igual a 120 caracteres y que solo contiene letras o espacios"),
    CODE_03("El sexo no es valido. Verifica que no esta vacio y que es M (macho) o H (hembra)"),
    CODE_04("La fecha no es valida. Verifica que no esta vacia y que es anterior o igual a la fecha actual"),
    //Used in Service
    CODE_O5("El nombre ya se encuentra en uso. Seleccione uno distinto"),
    CODE_06("El nombre del padre especificado no se encuentra registrado"),
    CODE_07("El nombre de los padres debe ser distinto"),
    CODE_08("El sexo del padre no corresponde al tipo de padre especificado (padre, madre)"),
    CODE_09("El nombre especificado ya se encuentra en uso. Seleccione otro"),
    CODE_10("Las caracteristicas no son validas. El peso debe estar entre 9-15 kg y la altura entre 100-130 cm"),
    CODE_11("El nombre especificado no se encuentra asociado a ningún animal");
    private String message;

}
