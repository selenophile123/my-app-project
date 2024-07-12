package com.example;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CountryLocatorTest {

    private CountryLocator locator;

    @Before
    public void setUp() throws IOException {
        locator = new CountryLocator("C:/Users/Monu sharma/my-app/src/main/resources/ne_10m_admin_0_countries.shp");
    }

    @Test
    public void testGetCountryCode() throws Exception {
        String countryCode = locator.getCountryCode(37.7749, -122.4194);
        assertEquals("US", countryCode);

        countryCode = locator.getCountryCode(51.5074, -0.1278);
        assertEquals("GB", countryCode);

        countryCode = locator.getCountryCode(-90.0, 0.0);
        assertNull(countryCode);
    }
}
