package pl.geolocal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Piotr on 2016-12-17.
 */
public class GeolocationOperations {

    public static String getPcIpAddress() throws IOException {
        URL connection = new URL("http://checkip.amazonaws.com/");
        URLConnection con = connection.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return reader.readLine();
    }
}
