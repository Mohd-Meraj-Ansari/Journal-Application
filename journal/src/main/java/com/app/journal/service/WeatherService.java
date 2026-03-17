package com.app.journal.service;

import com.app.journal.api.response.WeatherResponse;
import com.app.journal.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String APIKEY;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String urlTemplate = appCache.appCache.get("weather_api");

        if (urlTemplate == null) {
            throw new RuntimeException("weather_api URL not found in cache");
        }

        String URL = urlTemplate
                .replace("Key", APIKEY)
                .replace("CITY", city);
//        String URL = appCache.appCache.get("weather_api").replace("Key", APIKEY).replace("CITY", city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(URL, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }
}
