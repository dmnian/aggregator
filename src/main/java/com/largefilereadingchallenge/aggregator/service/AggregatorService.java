package com.largefilereadingchallenge.aggregator.service;

import com.largefilereadingchallenge.aggregator.domain.YearTemp;
import com.largefilereadingchallenge.aggregator.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private final Logger log = LoggerFactory.getLogger(AggregatorService.class);
    private Aggregator aggregator;
    private long previousModifiedTime = -1L;

    @Value("${file.name}")
    private String fileName;

    public void aggregate() {
        String path = getParentDirectoryFromJar();
        Path file = Paths.get(path, fileName);

        long lastModifiedTime = getLastModifiedTimeCurrentFile(file);
        boolean isFileChanged = lastModifiedTime != previousModifiedTime;

        if (isFileChanged) {
            log.info("Aggregation started");
            aggregator = new Aggregator();

            try (Stream<String> lines = Files.lines(file)) {
                lines.forEach(line -> {
                    final var record = line.split(";");

                    final var city = record[0];
                    final var year = record[1].split("-")[0];
                    final var temperature = Float.parseFloat(record[2]);

                    aggregator.aggregate(city, year, temperature);
                });

                log.info("Aggregation finished");
            } catch (IOException e) {
                log.error("Aggregation error");
                throw new CustomException(e);
            }

            previousModifiedTime = lastModifiedTime;
        }
    }

    private long getLastModifiedTimeCurrentFile(Path file) {
        try {
            final var lastModifiedTime = Files.readAttributes(file, BasicFileAttributes.class).lastModifiedTime().toMillis();
            log.info("lastModifiedTime: " + lastModifiedTime);

            return lastModifiedTime;
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    public List<YearTemp> getAvgTempForCity(String city) {
        aggregate();
        return aggregator.getAvgTemperatureByYearForCity(city);
    }

    private String getParentDirectoryFromJar() {
        String dirtyPath = getClass().getResource("").toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", "");
        jarPath = jarPath.replaceAll("jar!.*", "jar");
        jarPath = jarPath.replaceAll("%20", " ");
        if (!jarPath.endsWith(".jar")) {
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        String directoryPath = "/" + Paths.get(jarPath).getParent().toString();
        return directoryPath;
    }
}
