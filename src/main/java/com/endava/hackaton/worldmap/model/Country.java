package com.endava.hackaton.worldmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "countries")
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn
    private Measurement countryMeasurement;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<City> cityList;

    private LocalDateTime lastUpdatedDateTime;

    @PersistenceCreator
    public Country(String name) {
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

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public Measurement getCountryMeasurement() {
        return countryMeasurement;
    }

    public void setCountryMeasurement(Measurement countryMeasurement) {
        this.countryMeasurement = countryMeasurement;
    }

    public LocalDateTime getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(LocalDateTime lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }
}