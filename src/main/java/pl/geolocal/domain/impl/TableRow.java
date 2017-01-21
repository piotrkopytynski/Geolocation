package pl.geolocal.domain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by piotr on 12.01.2017.
 */
@Getter
@Setter
@AllArgsConstructor
public class TableRow {
    private String ipAddress;
    private Map<Date,Double> rttMeasurement = new LinkedHashMap<>();
    private Long distance;
    private String city;
    private String country;
    private Long ipHops;

    public TableRow(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public StringBuilder toStringBuilder() {
        return new StringBuilder(ipAddress + "," + country + "," + city + ","  + distance.toString());
    }
}
