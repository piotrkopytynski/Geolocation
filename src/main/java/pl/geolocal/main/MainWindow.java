package pl.geolocal.main;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.geolocal.domain.impl.Geolocation;
import pl.geolocal.service.DistanceService;
import pl.geolocal.service.GeolocationService;
import pl.geolocal.util.GeolocationOperations;
import pl.geolocal.util.IpValidator;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by piotr on 06.01.2017.
 */
@Getter
@Setter
@Component
public class MainWindow extends JFrame {

    private final GeolocationService geolocationService;
    private final DistanceService distanceService;
    private final IpValidator ipValidator;

    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTextField remoteIPtextField;
    private JButton calculateButton_panel1;
    private JTextField remoteIP1textField;
    private JTextField remoteIP2textField;
    private JButton calculateButton_panel2;
    private JLabel distanceLabel_panel1;


    @Autowired
    public MainWindow(GeolocationService geolocationService, DistanceService distanceService, IpValidator ipValidator) {
        this.geolocationService = geolocationService;
        this.distanceService = distanceService;
        this.ipValidator = ipValidator;
    }

    @PostConstruct
    public void init() {
        initComponents();
    }

    private void initComponents() {
        calculateButton_panel1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                String remoteIpAddress = remoteIPtextField.getText();
                String localIpAddress = GeolocationOperations.getPcIpAddress();
                if (!ipValidator.validate(rootPanel, remoteIpAddress)) {
                    Geolocation geolocationLocal = geolocationService.getJsonObject(localIpAddress);
                    Geolocation geolocationRemote = geolocationService.getJsonObject(remoteIpAddress);
                    distanceLabel_panel1.setText(distanceService.calculate(geolocationLocal, geolocationRemote));
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            }
        });

        calculateButton_panel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remoteIpAddres1 = remoteIP1textField.getText();
                String remoteIpAddres2 = remoteIP2textField.getText();
                try {
                    if (!(ipValidator.validate(rootPanel, remoteIpAddres1) ||
                            ipValidator.validate(rootPanel, remoteIpAddres2)) ) {
                        Geolocation geolocationRemote1 = geolocationService.getJsonObject(remoteIpAddres1);
                        Geolocation geolocationRemote2 = geolocationService.getJsonObject(remoteIpAddres2);
                        distanceService.calculate(geolocationRemote1, geolocationRemote2);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                }
        });
    }
}
