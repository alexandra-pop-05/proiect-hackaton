package com.endava.hackaton.worldmap.service;

import com.endava.hackaton.worldmap.model.City;
import com.endava.hackaton.worldmap.model.Country;
import com.endava.hackaton.worldmap.model.Measurement;
import com.endava.hackaton.worldmap.repository.CityRepo;
import com.endava.hackaton.worldmap.repository.CountryRepo;
import com.endava.hackaton.worldmap.repository.MeasurementRepo;
import com.endava.hackaton.worldmap.service.thread.WorkerThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class AqicnServiceImpl implements AqicnService {

    private final static Integer refreshTime = 30; // in minutes

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private MeasurementRepo measurementRepo;

    @Autowired
    private ExecutorService executorService;

    @Override
    public void processCountryAndCitiesAqi(Country country) throws ExecutionException, InterruptedException {

        if(countryRepo.findMeasurementsByName(country.getName()) != null){
            if(ChronoUnit.MINUTES.between(country.getLastUpdatedDateTime(), LocalDateTime.now()) >= refreshTime)
            {
                executorService.submit(() -> {
                    try {
                        processCountryMeasurements(country, processCitiesMeasurements(country));
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
                return;
            }
            return;
        }

        processCountryMeasurements(country, processCitiesMeasurements(country));

    }

    private void processCountryMeasurements(Country cntry, List<Measurement> measurements) {
        Integer averageAqi = (int) measurements.stream().map(Measurement::getAqi).filter(aqi -> !aqi.equals(-1)).mapToDouble(Integer::doubleValue).average().orElse(-1);
        Double averageTemp = measurements.stream().map(Measurement::getTemperature).mapToDouble(Double::doubleValue).average().orElse(-1);
        Double averageWind = measurements.stream().map(Measurement::getWind).mapToDouble(Double::doubleValue).average().orElse(-1);
        Double averagePressure = measurements.stream().map(Measurement::getPressure).mapToDouble(Double::doubleValue).average().orElse(-1);
        Double averageHumidity = measurements.stream().map(Measurement::getHumidity).mapToDouble(Double::doubleValue).average().orElse(-1);

        Long countryMeasurementId = countryRepo.findCountryMeasurementById(cntry.getId());
        Measurement measurement;
        if(countryMeasurementId != null){
            Optional<Measurement> optMeasurment = measurementRepo.findById(countryRepo.findCountryMeasurementById(cntry.getId()));
            measurement = optMeasurment.get();
            measurement.setAqi(averageAqi);
            measurement.setTemperature(averageTemp);
            measurement.setPressure(averagePressure);
            measurement.setHumidity(averageHumidity);
            measurement.setWind(averageWind);
        }
        else {
            measurement = new Measurement(averageAqi, averageTemp, averagePressure, averageHumidity, averageWind, true);
        }
        cntry.setCountryMeasurement(measurement);
        cntry.setLastUpdatedDateTime(LocalDateTime.now());
        measurementRepo.save(measurement);
        countryRepo.save(cntry);
    }

    private List<Measurement> processCitiesMeasurements(Country cntry) throws ExecutionException, InterruptedException {
        List<City> cities = cityRepo.findCitiesByCountry(cntry);

        List<Future<Object[]>> futures = new ArrayList<>();
        for(City city : cities){
            final Future<Object[]> future = executorService.submit(new WorkerThread(city));
            futures.add(future);
        }

        List<Measurement> measurements = new ArrayList<>();
        for(int i = 0; i < futures.size(); i++)
        {
            cities.get(i).setLastUpdatedDateTime(LocalDateTime.now());

            Long cityMeasurementId = cityRepo.findCityMeasurementById(cities.get(i).getId());
            Measurement measurement;
            if(cityMeasurementId != null){
                Optional<Measurement> optMeasurement = measurementRepo.findById(cityRepo.findCityMeasurementById(cities.get(i).getId()));
                measurement = optMeasurement.get();
                measurement.setAqi((Integer) futures.get(i).get()[0]);
                measurement.setTemperature((Double) futures.get(i).get()[1]);
                measurement.setPressure((Double) futures.get(i).get()[2]);
                measurement.setWind((Double) futures.get(i).get()[3]);
                measurement.setHumidity((Double) futures.get(i).get()[4]);
            }
            else{
               measurement = new Measurement(
                        (Integer) futures.get(i).get()[0],
                        (Double) futures.get(i).get()[1],
                        (Double) futures.get(i).get()[2],
                        (Double) futures.get(i).get()[4],
                        (Double) futures.get(i).get()[3],
                        false);
            }
            measurements.add(measurement);
            cities.get(i).setCityMeasurement(measurement);
            measurementRepo.save(measurement);
            cityRepo.save(cities.get(i));
        }
        return measurements;
    }


}
