package com.endava.hackaton.worldmap.service.thread;

import com.endava.hackaton.worldmap.model.City;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public record WorkerThread(City city) implements Callable<Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerThread.class);

    @Override
    public Object[] call() {
        LOG.info("Begin thread " + city.getName());
        return processThread(city);

    }

    private Object[] processThread(City city) {
        RestTemplate restTemplate = new RestTemplate();
        if(city.getGeoLon() == null)
            return getMeasurementsAPI1(restTemplate, city);
        else
            return getMeasurementsAPI2(restTemplate, city);
    }

    private Object[] getMeasurementsAPI1(RestTemplate restTemplate, City city){
        String url = "https://api.waqi.info/feed/" + city + "/?token=dcf6fadf-2191-49ab-89af-02b8ed0fb22c";
        String jsonStr = restTemplate.getForObject(url, String.class);
        if(jsonStr.contains("error")) return null;

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

    private Object[] getMeasurementsAPI2(RestTemplate restTemplate, City city){
        String url = "http://api.airvisual.com/v2/nearest_city?lat=" + city.getGeoLon() + "&lon=" + city.getGeoLat() + "&key=35b32cdc-5b6d-448a-a4b7-9a1eaca8993d";
        String jsonStr = restTemplate.getForObject(url, String.class);
        if(jsonStr.contains("fail")) return null;

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

