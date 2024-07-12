package com.example;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CountryLocator {

    private SimpleFeatureCollection featureCollection;
    private WKTReader wktReader;

    public CountryLocator(String shapefilePath) throws IOException {
        File file = new File(shapefilePath);
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
        if (dataStore == null) {
            throw new IOException("Unable to read shapefile: " + shapefilePath);
        }
        featureCollection = dataStore.getFeatureSource().getFeatures();
        wktReader = new WKTReader();
    }

    public String getCountryCode(double latitude, double longitude) {
        Point point = createPoint(latitude, longitude);

        try (SimpleFeatureIterator iterator = featureCollection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry.contains(point)) {
                    return feature.getAttribute("ISO_A2").toString(); // Adjust attribute name as per your shapefile
                }
            }
        }
        return null; // No country found for the given coordinates
    }

    private Point createPoint(double latitude, double longitude) {
        try {
            String wktPoint = String.format("POINT(%f %f)", longitude, latitude);
            return (Point) wktReader.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Error creating point from coordinates: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Prompt the user to enter latitude and longitude
            System.out.print("Enter latitude: ");
            double latitude = scanner.nextDouble();

            System.out.print("Enter longitude: ");
            double longitude = scanner.nextDouble();

            CountryLocator locator = new CountryLocator("C:/Users/Monu sharma/my-app/src/main/resources/ne_10m_admin_0_countries.shp");
            String countryCode = locator.getCountryCode(latitude, longitude);
            if (countryCode != null) {
                System.out.println("Country Code: " + countryCode);
            } else {
                System.out.println("No country found for the given coordinates.");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
