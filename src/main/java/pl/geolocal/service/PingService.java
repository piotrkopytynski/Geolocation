package pl.geolocal.service;

/**
 * Created by Piotr on 2017-01-06.
 */
public interface PingService {

    Double calculateRttValue(String remoteIpAddress);

    Integer tracerouteRemote(String remoteIpAddress);
}
