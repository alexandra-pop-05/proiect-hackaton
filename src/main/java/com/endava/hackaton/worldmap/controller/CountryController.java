package com.endava.hackaton.worldmap.controller;

import com.endava.hackaton.worldmap.exception.InvalidCountryNameException;
import com.endava.hackaton.worldmap.model.Country;
import com.endava.hackaton.worldmap.model.dto.request.CntryReqDTO;
import com.endava.hackaton.worldmap.model.dto.response.PopupDTO;
import com.endava.hackaton.worldmap.model.dto.response.RecyclePointsDTO;
import com.endava.hackaton.worldmap.repository.CountryRepo;
import com.endava.hackaton.worldmap.service.AqicnService;
import com.endava.hackaton.worldmap.service.mapper.PopupMapper;
import com.endava.hackaton.worldmap.service.mapper.RecyclePointsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class CountryController {

    @Autowired
    private PopupMapper mapper;

    @Autowired
    private RecyclePointsMapper recyclePointsMapper;
    @Autowired
    private AqicnService aqicnService;
    @Autowired
    private CountryRepo countryRepo;

    // POST http://localhost:8080/countries/measurements
    @PostMapping("/countries/measurements")
    public PopupDTO getCountryAqi(@Valid @RequestBody CntryReqDTO requestDTO) throws ExecutionException, InterruptedException {
        String countryName = requestDTO.getCountryName();

        Country country = countryRepo.findCountryByName(countryName);
        if(country == null){
            throw new InvalidCountryNameException("can't find country " + countryName + " in database");
        }

        aqicnService.processCountryAndCitiesAqi(country);
        return mapper.toDto(countryName);
    }

    @GetMapping("/countries/measurements")
    public PopupDTO getCountryAqiGet(@RequestParam String countryName) throws ExecutionException, InterruptedException {

        Country country = countryRepo.findCountryByName(countryName);
        if(country == null) {
            throw new InvalidCountryNameException("can't find country " + countryName + " in database");
        }


        aqicnService.processCountryAndCitiesAqi(country);

        return mapper.toDto(countryName);
    }

    @GetMapping("/countries/recyclePoints")
    public RecyclePointsDTO getRecyclingPoints(@RequestParam String countryName) {

        Country country = countryRepo.findCountryByName(countryName);
        if(country == null) {
            throw new InvalidCountryNameException("can't find country " + countryName + " in database");
        }

        return recyclePointsMapper.toDto(countryName);
    }


    // GET http://localhost:8080/countries/nocities
    @GetMapping("/countries/nocities")
    public List<String> getCountriesNoCities(){
       return countryRepo.findCountryNamesWithoutCities();
    }

}
