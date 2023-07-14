package com.largefilereadingchallenge.aggregator.service;

import com.largefilereadingchallenge.aggregator.domain.YearTemp;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AggregatorService {

    private Aggregator aggregator;
    private long previousModifiedTime = -1L;

    @PostConstruct
    void aggregate() {
        final var fileName = "src/main/resources/example_file2.csv";
        Path file = Paths.get(fileName);

        long lastModifiedTime = getLastModifiedTimeCurrentFile(file);
        boolean isFileChanged = lastModifiedTime != previousModifiedTime;

        if (isFileChanged) {
            System.out.println("Aggregating...");
            aggregator = new Aggregator();


            try (Stream<String> lines = Files.lines(file)) {
                lines.forEach(line -> {
                    final var record = line.split(";");

                    String city = record[0];
                    String year = record[1].split("-")[0];
                    var temperature = Float.parseFloat(record[2]);

                    aggregator.aggregate(city, year, temperature);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            previousModifiedTime = lastModifiedTime;
        }
    }

    private long getLastModifiedTimeCurrentFile(Path file) {
        try {
            var lastModifiedTime = Files.readAttributes(file, BasicFileAttributes.class).lastModifiedTime().toMillis();
            System.out.println("lastModifiedTime: " + lastModifiedTime);

            return lastModifiedTime;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<YearTemp> getAvgTempForCity(String city) {
        aggregate();
        return aggregator.getAvgTemperatureByYearForCity(city);
    }
}
