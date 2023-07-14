package com.largefilereadingchallenge.aggregator.controller;

import com.largefilereadingchallenge.aggregator.domain.YearTemp;
import com.largefilereadingchallenge.aggregator.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class YearAvgTempController {
    @Autowired
    private AggregatorService aggregatorService;

    @GetMapping("/city/{city}")
    List<YearTemp> get(@PathVariable("city") String city) {
        return aggregatorService.getAvgTempForCity(city);
    }
}
