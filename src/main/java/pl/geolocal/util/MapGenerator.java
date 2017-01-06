package pl.geolocal.util;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.geolocal.domain.impl.Geolocation;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by piotr on 06.01.2017.
 */
@Service
public class MapGenerator {

    private static final String URL = "http://maps.googleapis.com/maps/api/staticmap?";

    private static final String IMAGE = "image.jpg";

    private static final String CENTER = "center";
    private static final String ZOOM = "zoom";
    private static final String PATH = "path";
    private static final String SIZE = "size";
    private static final String FORMAT = "format";
    private static final String SCALE = "scale";
    private static final String KEY = "key";

    private static final String KEY_VALUE = "AIzaSyC_7K2aRloOdnzlrDTUMl6Oc_GRjPRZBhU";
    private static final String FORMAT_VALUE = "jpg";
    private static final String SCALE_VALUE = "2";
    private static final String SIZE_VALUE = "640x640";

    private static final String PATH_PARAMETERS = "color:0x0000ff|weight:5|";

    public Map<String, String> initUrlMap() {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put(SCALE, SCALE_VALUE);
        urlMap.put(SIZE, SIZE_VALUE);
        urlMap.put(KEY, KEY_VALUE);
        urlMap.put(FORMAT, FORMAT_VALUE);
        return urlMap;
    }

    public ImageIcon createLocaleMap(Geolocation geolocation, int zoom) {
        Map<String, String> urlMap = initUrlMap();
        urlMap.put(CENTER, getLocation(geolocation));
        urlMap.put(ZOOM, String.valueOf(zoom));

        return createMap(createMapUrl(urlMap));
    }



    public ImageIcon createPathMap(Geolocation geolocationSource, Geolocation geolocationDestination) {
        Map<String, String> urlMap = initUrlMap();
        String path = getPath(geolocationSource, geolocationDestination);
        urlMap.put(PATH, path);
        return createMap(createMapUrl(urlMap));
    }

    private String getPath(Geolocation geolocationSource, Geolocation geolocationDestination) {
        return PATH_PARAMETERS + getLocation(geolocationSource) + "|" + getLocation(geolocationDestination);
    }

    private String getLocation(Geolocation geolocation) {
        return geolocation.getLat() + "," + geolocation.getLon();
    }

    private StringBuilder createMapUrl(Map<String, String> urlMap) {
        StringBuilder imageUrl = new StringBuilder(URL);
        final Long[] count = {0L};
        urlMap.forEach((key, value) -> {
            if (count[0] == 0) {
                imageUrl.append(key).append("=").append(value);
            } else {
                imageUrl.append("&").append(key).append("=").append(value);
            }

            count[0]++;
        });
        return imageUrl;
    }

    private ImageIcon createMap(StringBuilder imageUrl) {
        try {
            String destinationFile = "image.jpg";
            URL url = new URL(String.valueOf(imageUrl));
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return new ImageIcon((new ImageIcon(IMAGE)).getImage()
                .getScaledInstance(640, 640, java.awt.Image.SCALE_SMOOTH));
    }
}
