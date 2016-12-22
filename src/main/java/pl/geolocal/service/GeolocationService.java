package pl.geolocal.service;

import pl.geolocal.domain.impl.Geolocation;

/**
 * Created by Piotr on 2016-12-18.
 */
public interface GeolocationService {
    Geolocation getJsonObject(String ipAddres) throws Exception;
}
