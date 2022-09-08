package com.endava.hackaton.worldmap.service.mapper;

import com.endava.hackaton.worldmap.model.Country;
import com.endava.hackaton.worldmap.model.dto.response.CityMeasDTO;
import com.endava.hackaton.worldmap.model.dto.response.GeoLocDTO;
import com.endava.hackaton.worldmap.model.dto.response.PopupDTO;
import com.endava.hackaton.worldmap.repository.CityRepo;
import com.endava.hackaton.worldmap.repository.CountryRepo;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PopupMapper {

    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private CityRepo cityRepo;

    public PopupDTO toDto(String countryName){

        Country country = countryRepo.findCountryByName(countryName);

        PopupDTO popupDTO = new PopupDTO();

        List<Object[]> measurements = countryRepo.findMeasurementsByName(countryName);
        popupDTO.setCountryAqi((Integer) measurements.get(0)[0]);
        popupDTO.setCountryTemp(Double.valueOf(String.format("%.2f", (Double) measurements.get(0)[1])));
        popupDTO.setCountryPressure(Double.valueOf(String.format("%.2f", (Double) measurements.get(0)[2])));
        popupDTO.setCountryHumidity(Double.valueOf(String.format("%.2f", (Double) measurements.get(0)[3])));
        popupDTO.setCountryWind(Double.valueOf(String.format("%.2f", (Double) measurements.get(0)[4])));

        List<Object[]> objAqiDesc = cityRepo.getNCitiesOrderByAqisDesc(3, country.getName());
        List<Object[]> objAqiAsc = cityRepo.getNCitiesOrderByAqis(3, country.getName());
        List<Object[]> objTempDesc = cityRepo.getNCitiesOrderByTempDesc(3, country.getName());
        List<Object[]> objTempAsc = cityRepo.getNCitiesOrderByTemp(3, country.getName());

        List<CityMeasDTO> top3CitiesAqiDesc = new ArrayList<>();
        List<CityMeasDTO> top3CitiesAqiAsc = new ArrayList<>();
        List<CityMeasDTO> top3CitiesTempDesc = new ArrayList<>();
        List<CityMeasDTO> top3CitiesTempAsc = new ArrayList<>();

        processTopCities(objAqiDesc, objAqiAsc, top3CitiesAqiDesc, top3CitiesAqiAsc);
        processTopCities(objTempDesc, objTempAsc, top3CitiesTempDesc, top3CitiesTempAsc);

        popupDTO.setTop3CitiesHighAqis(top3CitiesAqiDesc);
        popupDTO.setTop3CitiesLowAqis(top3CitiesAqiAsc);
        popupDTO.setTop3CitiesHighTemp(top3CitiesTempDesc);
        popupDTO.setTop3CitiesLowTemp(top3CitiesTempAsc);
        popupDTO.setLastUpdatedDateTime(String.valueOf(ChronoUnit.HOURS.between(country.getLastUpdatedDateTime(), LocalDateTime.now())));
        popupDTO.setStatusCode(HttpStatus.OK);
        return popupDTO;
    }

    private void processTopCities(List<Object[]> objDesc, List<Object[]> objAsc, List<CityMeasDTO> top3CitiesDesc, List<CityMeasDTO> top3CitiesAsc) {
        for(Object[] obj : objDesc){
            top3CitiesDesc.add(new CityMeasDTO((String)obj[0], (Integer)obj[1], (Double)obj[2]));
        }
        for(Object[] obj : objAsc){
            top3CitiesAsc.add(new CityMeasDTO((String)obj[0], (Integer)obj[1], (Double)obj[2]));
        }
    }

}
