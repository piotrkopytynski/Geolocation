package pl.geolocal.domain.impl;

import lombok.Getter;
import lombok.Setter;
import pl.geolocal.domain.Geolocation;

/**
 * Created by Piotr on 2016-12-18.
 */
@Getter
@Setter
public class GeolocationImpl implements Geolocation{
    private String region;

    private String zip;

    private String lon;

    private String status;

    private String query;

    private String isp;

    private String countryCode;

    private String regionName;

    private String as;

    private String org;

    private String city;

    private String country;

    private String timezone;

    private String lat;

    @Override
    public String toString()
    {
        return "GeolocationImpl [region = "+region+", zip = "+zip+", lon = "+lon+", status = "+status+", query = "+query+", isp = "+isp+", countryCode = "+countryCode+", regionName = "+regionName+", as = "+as+", org = "+org+", city = "+city+", country = "+country+", timezone = "+timezone+", lat = "+lat+"]";
    }
}
