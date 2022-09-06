package com.endava.hackaton.worldmap.service.mapper;

import com.endava.hackaton.worldmap.model.Country;
import com.endava.hackaton.worldmap.model.dto.response.GeoLocDTO;
import com.endava.hackaton.worldmap.model.dto.response.PopupDTO;
import com.endava.hackaton.worldmap.model.dto.response.RecyclePointsDTO;
import com.endava.hackaton.worldmap.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecyclePointsMapper {
    @Autowired
    private CountryRepo countryRepo;

    public RecyclePointsDTO toDto(String countryName) {
        Country country = countryRepo.findCountryByName(countryName);

        RecyclePointsDTO recyclePointsDTO = new RecyclePointsDTO();

        List<Object[]> objGeoLoc = countryRepo.findGeoLocationByName(countryName);
        List<GeoLocDTO> geoLocations = new ArrayList<>();
        for (Object[] objects : objGeoLoc) {
            geoLocations.add(new GeoLocDTO((Double) objects[0], (Double) objects[1], (String) objects[2]));
        }

        recyclePointsDTO.setRecyclePoints(geoLocations);
        return recyclePointsDTO;
    }
}
