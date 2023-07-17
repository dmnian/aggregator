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
        String warszawa = "Warszawa";
        aggregator.aggregate(warszawa, "1990", 12.25f);
        aggregator.aggregate(warszawa, "1990", 10.75f);
        aggregator.aggregate(warszawa, "1990", 12.50f);

        List<YearTemp> avgTemperatureByYearForCity = aggregator.getAvgTemperatureByYearForCity(warszawa);

        YearTemp yearTemp = avgTemperatureByYearForCity.stream().findFirst().get();

        assertEquals("1990", yearTemp.getYear());
        assertEquals(11.83f, yearTemp.getAverageTemperature());
    }
}
