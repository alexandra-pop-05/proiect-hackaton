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
        String url = "https://api.waqi.info/feed/" + city.getName() + "/?token=92f4b33e6870f407457f63bd7f7b27711eb93daa";
        String jsonStr = restTemplate.getForObject(url, String.class);

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

