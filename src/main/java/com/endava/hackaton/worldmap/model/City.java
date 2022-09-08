package com.endava.hackaton.worldmap.model;


import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="cities")
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column
    private Double geoLat;
    @Column
    private Double geoLon;
    @Column(columnDefinition = "boolean default true")
    private Boolean isLiveData;
    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn
    private Measurement cityMeasurement;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    private LocalDateTime lastUpdatedDateTime;

    @PersistenceCreator
    public City(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(Double geoLat) {
        this.geoLat = geoLat;
    }

    public Double getGeoLon() {
        return geoLon;
    }

    public void setGeoLon(Double geoLon) {
        this.geoLon = geoLon;
    }

    public Boolean getLiveData() {
        return isLiveData;
    }

    public void setLiveData(Boolean liveData) {
        isLiveData = liveData;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Measurement getCityMeasurement() {
        return cityMeasurement;
    }

    public void setCityMeasurement(Measurement cityMeasurement) {
        this.cityMeasurement = cityMeasurement;
    }

    public LocalDateTime getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(LocalDateTime lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }
}
