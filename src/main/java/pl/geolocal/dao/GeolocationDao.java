package pl.geolocal.dao;

import pl.geolocal.domain.impl.Geolocation;

/**
 * Created by Piotr on 2016-12-18.
 */
public interface GeolocationDao {
    Geolocation getJsonObject(String ipAddress);
}
