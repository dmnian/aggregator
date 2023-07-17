package com.largefilereadingchallenge.aggregator.domain;

public class YearTemp {
    private final String year;
    private float aggregatedTemp;
    private int counter;

    public YearTemp(String year, float firstTemperatureMeasurement) {
        this.year = year;
        this.aggregatedTemp = firstTemperatureMeasurement;
        this.counter = 1;
    }

    public void addTemperatureMeasurement(float temp) {
        aggregatedTemp += temp;
        counter++;
    }

    public float getAverageTemperature() {
        return (float) (Math.round((aggregatedTemp / counter) * 100) / 100.0);
    }

    public String getYear() {
        return this.year;
    }

    @Override
    public String toString() {
        return "YearTemp{" +
                "year='" + year + "'," +
                "averageTemperature='" + this.getAverageTemperature() + '\'' +
                '}';
    }
}