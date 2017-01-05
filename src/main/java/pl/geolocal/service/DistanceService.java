package pl.geolocal.service;

import pl.geolocal.domain.impl.Geolocation;

/**
 * Created by piotr on 05.01.2017.
 */
public interface DistanceService {
    String calculate(Geolocation source, Geolocation destination);
}
