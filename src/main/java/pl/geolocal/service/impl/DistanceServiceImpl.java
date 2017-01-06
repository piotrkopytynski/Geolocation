package pl.geolocal.service.impl;

import org.springframework.stereotype.Service;
import pl.geolocal.domain.impl.Geolocation;
import pl.geolocal.service.DistanceService;

/**
 * Created by piotr on 05.01.2017.
 */
@Service
public class DistanceServiceImpl implements DistanceService {

    private static final String UNIT = " km";
    private static final int EARTH_RADIUS = 6371;

    @Override
    public String calculate(Geolocation source, Geolocation destination) {
        Double latDistance = Math.toRadians(Double.valueOf(destination.getLat()) - Double.valueOf(source.getLat()));
        Double lonDistance = Math.toRadians(Double.valueOf(destination.getLon()) - Double.valueOf(source.getLon()));
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(Double.valueOf(source.getLat()))) * Math.cos(Math.toRadians(
                        Double.valueOf(destination.getLat())))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        distance = Math.pow(distance, 2);

        return String.valueOf(Math.round(Math.sqrt(distance))) + UNIT;
    }
}
