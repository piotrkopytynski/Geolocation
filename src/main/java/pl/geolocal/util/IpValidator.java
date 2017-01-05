package pl.geolocal.util;

import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * Created by piotr on 05.01.2017.
 */
@Service
public class IpValidator {

    public boolean validate(JPanel jPanel, String ipAddress) {
        if(!isEmpty(jPanel, ipAddress)) {
           return isIncorrectFormat(jPanel, ipAddress);
        } else  {
            return true;
        }

    }

    private boolean isEmpty(JPanel jPanel ,String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            JOptionPane.showMessageDialog(jPanel, "IP address cannot be empty");
            return true;
        }
        return false;
    }

    private boolean isIncorrectFormat(JPanel jPanel ,String ipAddress) {
        final Pattern PATTERN = Pattern.compile(
                "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        if(!PATTERN.matcher(ipAddress).matches()) {
            JOptionPane.showMessageDialog(jPanel, "Incorrect format of ip address");
            return true;
        }
        return false;
    }
}
