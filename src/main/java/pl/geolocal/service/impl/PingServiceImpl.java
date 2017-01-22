package pl.geolocal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.geolocal.service.PingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Piotr on 2017-01-06.
 */
@Service
public class PingServiceImpl implements PingService {

    @Override
    public Double calculateRttValue(String remoteIpAddress) {

        Double sum = 0.0;
        Integer elements = 0;
        String line;
        Process sysProcess;
        String operatingSystem = System.getProperty("os.name");

        try {
            sysProcess = operatingSystem.contains("Windows") ? Runtime.getRuntime().exec("ping " + remoteIpAddress) :
                    Runtime.getRuntime().exec("ping " + remoteIpAddress + " -c 2");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("time=")) {
                    sum = sum + parseRttValueFromCmdLine(line);
                    elements++;
                }
            }
            if (elements == 0) {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sum / elements;
    }

    private Double parseRttValueFromCmdLine(String line) {
        return Double.valueOf(line.substring(line.lastIndexOf("time=") + 5).split("ms", 2)[0]);
    }

    public Integer tracerouteRemote(String remoteIpAddress) {
        int ipHops = 0;
        String line;
        Process sysProcess;
        String operatingSystem = System.getProperty("os.name");

        try {
            sysProcess = operatingSystem.contains("Windows") ? Runtime.getRuntime().exec("tracert " + remoteIpAddress) :
                    Runtime.getRuntime().exec("traceroute " + remoteIpAddress);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));

            while ((line = bufferedReader.readLine()) != null) {
                if (!StringUtils.isEmpty(line)) {
                    if (Character.isDigit(line.charAt(0)) || Character.isDigit(line.charAt(1)) || Character.isDigit(line.charAt(2))) {
                        ipHops++;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipHops;

    }
}
