package com.largefilereadingchallenge.aggregator;

import com.largefilereadingchallenge.aggregator.domain.YearTemp;
import com.largefilereadingchallenge.aggregator.service.Aggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregatorApplicationTests {

    private Aggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new Aggregator();
    }

    @Test
    void aggregate() {
        //given
        String city = "Warszawa";
        String year = "1990";

        float temp1 = 12.25f;
        float temp2 = 10.75f;
        float temp3 = 12.50f;

        //when
        aggregator.aggregate(city, year, temp1);
        aggregator.aggregate(city, year, temp2);
        aggregator.aggregate(city, year, temp3);

        List<YearTemp> avgTemperatureByYearForCity = aggregator.getAvgTemperatureByYearForCity(city);

        YearTemp result = avgTemperatureByYearForCity.stream().findFirst().get();

        //then
        assertEquals(year, result.getYear());
        assertEquals(11.83f, result.getAverageTemperature());
    }
}
