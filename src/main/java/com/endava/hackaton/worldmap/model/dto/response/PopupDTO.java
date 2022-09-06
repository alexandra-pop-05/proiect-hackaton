package com.endava.hackaton.worldmap.model.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class PopupDTO {

    private Integer countryAqi;
    private Double countryTemp;
    private Double countryPressure;
    private Double countryHumidity;
    private Double countryWind;
    private List<CityMeasDTO> top3CitiesHighAqis; // 99, 98, 97, ...
    private List<CityMeasDTO> top3CitiesLowAqis; // 1, 2, 3, ...
    private List<CityMeasDTO> top3CitiesHighTemp;
    private List<CityMeasDTO> top3CitiesLowTemp;
    private String lastUpdatedDateTime;
    private HttpStatus statusCode;


}
