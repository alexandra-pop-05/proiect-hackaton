package com.endava.hackaton.worldmap.repository;

import com.endava.hackaton.worldmap.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryRepo extends JpaRepository<Country, Long> {

    Country findCountryByName(String name);

    @Query(value = "SELECT country_measurement_id FROM worldmapdb.countries WHERE id = :id", nativeQuery = true)
    Long findCountryMeasurementById(@Param("id") Long id);

    @Query(value = "SELECT t1.name FROM worldmapdb.countries t1 LEFT JOIN worldmapdb.cities t2 ON t1.id = t2.country_id WHERE t2.id is NULL", nativeQuery = true)
    List<String> findCountryNamesWithoutCities();

    @Query(value = "SELECT t2.aqi, t2.temperature, t2.pressure, t2.humidity, t2.wind FROM worldmapdb.countries t1 JOIN worldmapdb.measurements t2 ON t1.country_measurement_id = t2.id JOIN worldmapdb.geolocations t3 ON t2.id = t3.measurement_id where name = :name LIMIT 1;", nativeQuery = true)
    List<Object[]> findMeasurementsByName(@Param("name") String countryName);

    @Query(value = "SELECT t1.latitude, t1.longitude, t1.details FROM worldmapdb.geolocations t1 join worldmapdb.countries t2 on t1.measurement_id = t2.country_measurement_id where t2.name = :name", nativeQuery = true)
    List<Object[]> findGeoLocationByName(@Param("name") String countryName);

}