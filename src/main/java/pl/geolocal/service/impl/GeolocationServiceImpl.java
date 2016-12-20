package pl.geolocal.service.impl;

import org.springframework.stereotype.Service;
import pl.geolocal.dao.GeolocationDao;
import pl.geolocal.dao.impl.GeolocationDaoImpl;
import pl.geolocal.domain.impl.GeolocationImpl;
import pl.geolocal.service.GeolocationService;

/**
 * Created by Piotr on 2016-12-18.
 */
@Service("geolocationService")
public class GeolocationServiceImpl implements GeolocationService {

//    @Resource(name = "geolocationDao")
//    GeolocationDao geolocationDao;

    public GeolocationImpl getJsonObject(String ipAddress) throws Exception {
        GeolocationDao geolocationDao = new GeolocationDaoImpl();
        return geolocationDao.getJsonObject(ipAddress);
    }
}