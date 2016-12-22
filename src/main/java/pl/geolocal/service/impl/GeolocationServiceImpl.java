package pl.geolocal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.geolocal.dao.GeolocationDao;
import pl.geolocal.domain.impl.Geolocation;
import pl.geolocal.service.GeolocationService;

/**
 * Created by Piotr on 2016-12-18.
 */
@Service
public class GeolocationServiceImpl implements GeolocationService {

    private final GeolocationDao geolocationDao;

    @Autowired
    public GeolocationServiceImpl(GeolocationDao geolocationDao) {
        this.geolocationDao = geolocationDao;
    }

    @Override
    public Geolocation getJsonObject(String ipAddress) throws Exception {
        return geolocationDao.getJsonObject(ipAddress);
    }
}