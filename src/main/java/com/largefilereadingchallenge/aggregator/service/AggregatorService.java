package com.largefilereadingchallenge.aggregator.service;

import com.largefilereadingchallenge.aggregator.domain.YearTemp;
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

    private Aggregator aggregator;
    private long previousModifiedTime = -1L;

    @Value("${file.name}")
    private String fileName;

    public void aggregate() {
        String path = getParentDirectoryFromJar();
        System.out.println(path);
        System.out.println("!!! filename: "+fileName);
        Path file = Paths.get(path, fileName);


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

    private String getParentDirectoryFromJar() {
        String dirtyPath = getClass().getResource("").toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", ""); //removes file:/ and everything before it
        jarPath = jarPath.replaceAll("jar!.*", "jar"); //removes everything after .jar, if .jar exists in dirtyPath
        jarPath = jarPath.replaceAll("%20", " "); //necessary if path has spaces within
        if (!jarPath.endsWith(".jar")) { // this is needed if you plan to run the app using Spring Tools Suit play button.
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        String directoryPath = "/"+Paths.get(jarPath).getParent().toString(); //Paths - from java 8
        return directoryPath;
    }
}
