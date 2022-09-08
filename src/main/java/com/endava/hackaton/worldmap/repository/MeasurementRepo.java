package com.endava.hackaton.worldmap.repository;

import com.endava.hackaton.worldmap.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepo extends JpaRepository<Measurement, Long> {

}
