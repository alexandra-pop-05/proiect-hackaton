package com.endava.hackaton.worldmap.repository;

import com.endava.hackaton.worldmap.model.City;
import com.endava.hackaton.worldmap.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepo extends JpaRepository<City, Long> {

    List<City> findCitiesByCountry(Country country);

    @Query(value = "SELECT city_measurement_id FROM worldmapdb.cities WHERE id = :id", nativeQuery = true)
    Long findCityMeasurementById(@Param("id") Long id);

    @Query(value = "SELECT t1.name, t3.aqi, t3.temperature FROM worldmapdb.cities t1 JOIN worldmapdb.countries t2 ON t1.country_id = t2.id JOIN worldmapdb.measurements t3 ON t1.city_measurement_id = t3.id WHERE t2.name = :name AND t3.aqi IS NOT NULL AND t3.aqi != -1 AND t3.is_country_city = 0 ORDER BY t3.aqi DESC LIMIT :n", nativeQuery = true)
    List<Object[]> getNCitiesOrderByAqisDesc(@Param("n") int n, @Param("name") String name);
    @Query(value = "SELECT t1.name, t3.aqi, t3.temperature FROM worldmapdb.cities t1 JOIN worldmapdb.countries t2 ON t1.country_id = t2.id JOIN worldmapdb.measurements t3 ON t1.city_measurement_id = t3.id WHERE t2.name = :name AND t3.aqi IS NOT NULL AND t3.aqi != -1 AND t3.is_country_city = 0 ORDER BY t3.aqi LIMIT :n", nativeQuery = true)
    List<Object[]> getNCitiesOrderByAqis(@Param("n") int n, @Param("name") String name);

    @Query(value = "SELECT t1.name, t3.aqi, t3.temperature FROM worldmapdb.cities t1 JOIN worldmapdb.countries t2 ON t1.country_id = t2.id JOIN worldmapdb.measurements t3 ON t1.city_measurement_id = t3.id WHERE t2.name = :name AND t3.aqi IS NOT NULL AND t3.temperature != -1 AND t3.is_country_city = 0 ORDER BY t3.temperature DESC LIMIT :n", nativeQuery = true)
    List<Object[]> getNCitiesOrderByTempDesc(@Param("n") int n, @Param("name") String name);
    @Query(value = "SELECT t1.name, t3.aqi, t3.temperature FROM worldmapdb.cities t1 JOIN worldmapdb.countries t2 ON t1.country_id = t2.id JOIN worldmapdb.measurements t3 ON t1.city_measurement_id = t3.id WHERE t2.name = :name AND t3.aqi IS NOT NULL AND t3.temperature != -1 AND t3.is_country_city = 0 ORDER BY t3.temperature LIMIT :n", nativeQuery = true)
    List<Object[]> getNCitiesOrderByTemp(@Param("n") int n, @Param("name") String name);
}
