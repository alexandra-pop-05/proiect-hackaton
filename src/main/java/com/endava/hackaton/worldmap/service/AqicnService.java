package com.endava.hackaton.worldmap.service;

import com.endava.hackaton.worldmap.model.Country;

import java.util.concurrent.ExecutionException;

public interface AqicnService {

    void processCountryAndCitiesAqi(Country country) throws ExecutionException, InterruptedException;
}
