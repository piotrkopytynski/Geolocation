package pl.geolocal.util;

import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.geolocal.domain.impl.Geolocation;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * Created by piotr on 05.01.2017.
 */
@Service
public class IpValidator {

    public boolean validate(JPanel jPanel, String ipAddress) {
        if (!isEmpty(jPanel, ipAddress)) {
            return isIncorrectFormat(jPanel, ipAddress);
        }
        {
            return true;
        }
    }

    public boolean validateExistence(JPanel jPanel, Geolocation geolocation, String ipAddress) {
        if (geolocation.getStatus().equals("fail")) {
            JOptionPane.showMessageDialog(jPanel, "IP address " + ipAddress + " does not exist");
            return false;
        }
        return true;
    }

    private boolean isEmpty(JPanel jPanel, String ipAddress) {
        if (StringUtils.isEmpty(ipAddress)) {
            JOptionPane.showMessageDialog(jPanel, "IP address cannot be empty");
            return true;
        }
        return false;
    }

    private boolean isIncorrectFormat(JPanel jPanel, String ipAddress) {
        final Pattern PATTERN = Pattern.compile(
                "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        if (!PATTERN.matcher(ipAddress).matches()) {
            JOptionPane.showMessageDialog(jPanel, "Incorrect format of ip address");
            return true;
        }
        return false;
    }
}
