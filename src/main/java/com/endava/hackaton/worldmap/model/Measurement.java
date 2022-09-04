package com.endava.hackaton.worldmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "measurements")
@NoArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Integer aqi;
    @Column
    private Double temperature;
    @Column
    private Double pressure;
    @Column
    private Double humidity;
    @Column
    private Double wind;
    @JsonIgnore
    @OneToMany(mappedBy = "measurement")
    private List<GeoLocation> geoLocations;
    @Column
    private Boolean isCountryCity; // true = country, false = city
    @OneToOne(fetch = FetchType.LAZY)
    private Country country;
    @OneToOne(fetch = FetchType.LAZY)
    private City city;

    @PersistenceCreator
    public Measurement(Integer aqi, Double temperature, Double pressure, Double humidity, Double wind, Boolean isCountryCity) {
        this.aqi = aqi;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.isCountryCity = isCountryCity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAqi() {
        return aqi;
    }

    public void setAqi(Integer aqi) {
        this.aqi = aqi;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWind() {
        return wind;
    }

    public void setWind(Double wind) {
        this.wind = wind;
    }

    public List<GeoLocation> getGeoLocations() {
        return geoLocations;
    }

    public void setGeoLocations(List<GeoLocation> geoLocations) {
        this.geoLocations = geoLocations;
    }

    public Boolean getCountryCity() {
        return isCountryCity;
    }

    public void setCountryCity(Boolean countryCity) {
        isCountryCity = countryCity;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
