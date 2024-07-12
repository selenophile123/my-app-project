package com.example;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    public static void main(String[] args) {
        CountryLocator countryLocator;
        try {
            countryLocator = new CountryLocator("C:/Users/Monu sharma/my-app/src/main/resources/ne_10m_admin_0_countries.shp");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int requestCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        long startTime = System.nanoTime();

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    countryLocator.getCountryCode(37.7749, -122.4194); // Example coordinates
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor service did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        double averageTime = (double) duration / requestCount;

        System.out.println("Average execution time: " + averageTime + " ms");

        // Ensure all non-daemon threads are terminated
        System.exit(0);
    }
}
