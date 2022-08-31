package com.endava.hackaton.worldmap.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityMeasDTO {

    private String cityName;
    private Integer aqi;
    private Double temperature;
}
