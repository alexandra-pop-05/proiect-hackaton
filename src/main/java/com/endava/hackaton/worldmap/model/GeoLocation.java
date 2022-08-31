package com.endava.hackaton.worldmap.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "geolocations")
@NoArgsConstructor
public class GeoLocation {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private String details;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "measurement_id", referencedColumnName = "id")
    private Measurement measurement;

    @PersistenceCreator
    public GeoLocation(Double latitude, Double longitude, String details) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String name) {
        this.details = name;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }
}
