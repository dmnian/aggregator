package com.largefilereadingchallenge.aggregator.service;

import com.largefilereadingchallenge.aggregator.domain.YearTemp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregator {
    private final Map<String, Map<String, YearTemp>> cityYearMap = new HashMap<>();

    public void aggregate(String city, String year, float temp) {

        if (cityYearMap.containsKey(city)) {
            final Map<String, YearTemp> yearsForSpecificCityMap = cityYearMap.get(city);

            if (yearsForSpecificCityMap.containsKey(year)) {
                yearsForSpecificCityMap.get(year).addTemperatureMeasurement(temp);
            } else {
                yearsForSpecificCityMap.put(year, new YearTemp(year, temp));
            }
        } else {
            final var yearsForSpecificCityMap = new HashMap<String, YearTemp>();

            yearsForSpecificCityMap.put(year, new YearTemp(year, temp));
            cityYearMap.put(city, yearsForSpecificCityMap);
        }
    }

    public List<YearTemp> getAvgTemperatureByYearForCity(String city) {
        final Map<String, YearTemp> yearMap = cityYearMap.get(city);
        return yearMap.values().stream().toList();
    }
}