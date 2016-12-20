package pl.geolocal.dao;

import pl.geolocal.domain.impl.GeolocationImpl;

/**
 * Created by Piotr on 2016-12-18.
 */
public interface GeolocationDao {
    GeolocationImpl getJsonObject(String ipAddres);
}
