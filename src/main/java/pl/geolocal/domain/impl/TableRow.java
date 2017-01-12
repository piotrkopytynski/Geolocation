package pl.geolocal.domain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by piotr on 12.01.2017.
 */
@Getter
@Setter
@AllArgsConstructor
public class TableRow {
    private String ipAddress;
    private String domainName;
    private Double rttTime;
    private Long distance;
    private String City;
    private String Country;

    public TableRow(String ipAddress) {
        this.ipAddress = ipAddress;
        this.domainName = getDomainName(ipAddress);
    }

    private static String getDomainName(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "not exists";
        }
    }
}
