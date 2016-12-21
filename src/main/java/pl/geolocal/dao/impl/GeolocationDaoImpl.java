package pl.geolocal.dao.impl;

import com.google.gson.Gson;
import org.springframework.stereotype.Repository;
import pl.geolocal.dao.GeolocationDao;
import pl.geolocal.domain.impl.Geolocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Piotr on 2016-12-18.
 */
@Repository
public class GeolocationDaoImpl implements GeolocationDao {

    @Override
    public Geolocation getJsonObject(String ipAddress) {

        String json = null;
        try {
            json = readUrl("http://ip-api.com/json/"+ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        return gson.fromJson(json, Geolocation.class);
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
