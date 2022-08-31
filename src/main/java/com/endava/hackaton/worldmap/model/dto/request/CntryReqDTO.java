package com.endava.hackaton.worldmap.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CntryReqDTO {

    @NotBlank(message = "must not be blank or null")
    private String countryName;

}
