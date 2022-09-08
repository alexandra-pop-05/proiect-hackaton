package com.endava.hackaton.worldmap.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoLocDTO {

    private Double geoLatitude;
    private Double geoLongitude;
    private String geoDetails;

}
