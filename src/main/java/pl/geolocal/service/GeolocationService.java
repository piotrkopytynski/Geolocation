package pl.geolocal.service;

import pl.geolocal.domain.impl.GeolocationImpl;

/**
 * Created by Piotr on 2016-12-18.
 */
public interface GeolocationService {
    GeolocationImpl getJsonObject(String ipAddres) throws Exception;
}
