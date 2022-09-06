package com.endava.hackaton.worldmap.service.thread;

import com.endava.hackaton.worldmap.model.City;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.Callable;

public record WorkerThread(City city, RestTemplate restTemplate) implements Callable<Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerThread.class);


    @Override
    public Object[] call() throws InterruptedException {
        LOG.info("Begin thread " + city.getName());
        return processThread(city, restTemplate);

    }

    private Object[] processThread(City city, RestTemplate restTemplate) throws InterruptedException {
        if(city.getGeoLon() == null)
            return getMeasurementsAPI1(restTemplate, city);

        return getMeasurementsAPI2(restTemplate, city);

    }

    private Object[] getMeasurementsAPI1(RestTemplate restTemplate, City city){
        String url = "https://api.waqi.info/feed/" + city.getName() + "/?token=1abc22e9c4f22c13db8dff8fc7eaee60b93aeda4";
        boolean ok = false;
        String jsonStr = "";
        while(!ok) {
            try {
                jsonStr = restTemplate.getForObject(url, String.class);
                ok = true;
            } catch (ResourceAccessException ex) {
                LOG.info("Thread " + city.getName() + " timed out! Trying again...");
            }
        }
        if(jsonStr.contains("error")) {
            LOG.info("Fail thread " + city.getName());
            return null;
        }

        String aqi = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject().get("aqi").getAsString();
        String t = getJsonObj(jsonStr, "\"t\":{\"v\":", "t");
        String w = getJsonObj(jsonStr, "\"w\":{\"v\":", "w");
        String p = getJsonObj(jsonStr, "\"p\":{\"v\":", "p");
        String h = getJsonObj(jsonStr, "\"h\":{\"v\":", "h");

        int intAqi = !aqi.equals("-") ? Integer.parseInt(aqi) : -1;
        double doubleTemp = t != null ? Double.parseDouble(t) : -1;
        double doubleP = p != null ? Double.parseDouble(p) : 0;
        double doubleW = w != null ? Double.parseDouble(w) : 0;
        double doubleH = h != null ? Double.parseDouble(h) : 0;

        LOG.info("End thread " + city.getName());
        return new Object[]{intAqi, doubleTemp, doubleP, doubleW, doubleH};
    }

    private Object[] getMeasurementsAPI2(RestTemplate restTemplate, City city) throws InterruptedException {
        String url = "http://api.airvisual.com/v2/nearest_city?lat=" + city.getGeoLon() + "&lon=" + city.getGeoLat() + "&key=dcf6fadf-2191-49ab-89af-02b8ed0fb22c";
        boolean ok = false;
        String jsonStr = "";
        while(!ok) {
            try {
                jsonStr = restTemplate.getForObject(url, String.class);
                ok = true;
            } catch (Exception ex) {
                LOG.info(ex.getMessage());
                LOG.info("Thread " + city.getName() + " timed out! Trying again...");
                Thread.sleep(30000);
            }
        }
        if(jsonStr.contains("fail")) {
            LOG.info("Fail thread " + city.getName());
            return null;
        }

        String aqi = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject().get("current").getAsJsonObject().get("pollution").getAsJsonObject().get("aqius").getAsString();
        String t = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject().get("current").getAsJsonObject().get("weather").getAsJsonObject().get("tp").getAsString();
        String w = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject().get("current").getAsJsonObject().get("weather").getAsJsonObject().get("ws").getAsString();
        String p = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject().get("current").getAsJsonObject().get("weather").getAsJsonObject().get("pr").getAsString();
        String h = JsonParser.parseString(jsonStr).getAsJsonObject().get("data").getAsJsonObject().get("current").getAsJsonObject().get("weather").getAsJsonObject().get("hu").getAsString();

        int intAqi = !aqi.equals("-") ? Integer.parseInt(aqi) : -1;
        double doubleTemp = t != null ? Double.parseDouble(t) : -1;
        double doubleP = p != null ? Double.parseDouble(p) : 0;
        double doubleW = w != null ? Double.parseDouble(w) : 0;
        double doubleH = h != null ? Double.parseDouble(h) : 0;

        LOG.info("End thread " + city.getName());
        return new Object[]{intAqi, doubleTemp, doubleP, doubleW, doubleH};
    }

    private String getJsonObj(String jsonStr, String str, String measurement) {
        String result = null;
        if (jsonStr.contains(str)) {
            result = JsonParser.parseString(jsonStr).getAsJsonObject()
                    .get("data").getAsJsonObject()
                    .get("iaqi").getAsJsonObject()
                    .get(measurement).getAsJsonObject()
                    .get("v").getAsString();
        }
        return result;
    }


}

