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
import pl.geolocal.util.MapGenerator;

import javax.annotation.PostConstruct;
import javax.swing.*;

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
    private final MapGenerator mapGenerator;

    private String localIpAddress;
    private Geolocation geolocationLocal;

    private JPanel rootPanel;
    private JTabbedPane tabbedPanel;
    private JTextField remoteIpAddressTextField;
    private JButton calculateButton_panel1;
    private JTextField remoteIP1textField;
    private JTextField remoteIP2textField;
    private JButton calculateButton_panel2;
    private JLabel localIpAddressLabel;
    private JLabel localCityLabel;
    private JLabel remoteCityLabel;
    private JLabel distanceLabel_panel1;
    private JPanel mapPanel_panel1;
    private JLabel mapLabel_panel1;
    private JLabel remoteCountryLabel;
    private JLabel localCountryLabel;
    private JLabel localASLabel;
    private JLabel localOrgLabel;
    private JLabel remoteOrgLabel;
    private JLabel remoteASLabel;
    private JTextField sourceIpAddressTextField;
    private JTextField destinationIpAddressTextField;
    private JLabel mapLabel_panel2;
    private JLabel countrySourceLabel;
    private JLabel citySourceLabel;
    private JLabel orgSourceLabel;
    private JLabel asSourceLabel;
    private JLabel countryDestinationLabel;
    private JLabel cityDestinationLabel;
    private JLabel orgDestinationLabel;
    private JLabel asDestinationLabel;
    private JLabel distanceLabel_panel2;
    private JPanel localPanel;
    private JPanel remotePanel;
    private JPanel remoteMapPanel;
    private JLabel mapLabel;


    @Autowired
    public MainWindow(GeolocationService geolocationService, DistanceService distanceService, IpValidator ipValidator,
                      MapGenerator mapGenerator) {
        this.geolocationService = geolocationService;
        this.distanceService = distanceService;
        this.ipValidator = ipValidator;
        this.mapGenerator = mapGenerator;
    }

    @PostConstruct
    public void init() {
        initComponents();
    }

    private void initComponents() {

        setResizable(false);

        initLocalInformation();

        initDefaultMaps();

        calculateButton_panel1.addActionListener(e ->
                calculateLocalAction()
        );

        calculateButton_panel2.addActionListener(e ->
                calculateRemoteAction()
        );
    }

    private void calculateRemoteAction() {
        String remoteIpAddress1 = sourceIpAddressTextField.getText();
        String remoteIpAddress2 = destinationIpAddressTextField.getText();
        calculateRemote(remoteIpAddress1, remoteIpAddress2);
    }

    private void calculateRemote(String remoteIpAddress1, String remoteIpAddress2) {
        try {
            if (!(ipValidator.validate(rootPanel, remoteIpAddress1) ||
                    ipValidator.validate(rootPanel, remoteIpAddress2))) {
                Geolocation geolocationRemote1 = geolocationService.getJsonObject(remoteIpAddress1);
                Geolocation geolocationRemote2 = geolocationService.getJsonObject(remoteIpAddress2);
                if ((ipValidator.validateExistence(rootPanel, geolocationRemote1, remoteIpAddress1) ||
                        ipValidator.validateExistence(rootPanel, geolocationRemote2, remoteIpAddress2))) {
                    setRemoteInformation(geolocationRemote1, geolocationRemote2);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void setRemoteInformation(Geolocation geolocationRemote1, Geolocation geolocationRemote2) {
        distanceLabel_panel2.setText(distanceService.calculate(geolocationRemote1, geolocationRemote2));

        countrySourceLabel.setText(geolocationRemote1.getCountry());
        citySourceLabel.setText(geolocationRemote1.getCity());
        orgSourceLabel.setText(geolocationRemote1.getOrg());
        asSourceLabel.setText(geolocationRemote1.getAs());

        countryDestinationLabel.setText(geolocationRemote2.getCountry());
        cityDestinationLabel.setText(geolocationRemote2.getCity());
        orgDestinationLabel.setText(geolocationRemote2.getOrg());
        asDestinationLabel.setText(geolocationRemote2.getAs());

        mapLabel_panel2.setIcon(mapGenerator.createPathMap(geolocationRemote1, geolocationRemote2));
    }

    private void calculateLocalAction() {
        String remoteIpAddress = remoteIpAddressTextField.getText();
        calculateLocale(remoteIpAddress);
    }

    private void calculateLocale(String remoteIpAddress) {
        if (!ipValidator.validate(rootPanel, remoteIpAddress)) {
            Geolocation geolocationRemote = null;
            try {
                geolocationRemote = geolocationService.getJsonObject(remoteIpAddress);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (ipValidator.validateExistence(rootPanel, geolocationRemote, remoteIpAddress)) {
                setLocalInformation(geolocationRemote);
            }
        }
    }

    private void setLocalInformation(Geolocation geolocationRemote) {
        distanceLabel_panel1.setText(distanceService.calculate(geolocationLocal, geolocationRemote));
        remoteCountryLabel.setText(geolocationRemote.getCountry());
        remoteCityLabel.setText(geolocationRemote.getCity());
        remoteOrgLabel.setText(geolocationRemote.getOrg());
        remoteASLabel.setText(geolocationRemote.getAs());
        mapLabel_panel1.setIcon(mapGenerator.createPathMap(geolocationLocal, geolocationRemote));
    }

    private void initDefaultMaps() {
        mapLabel_panel1.setIcon(mapGenerator.createLocaleMap(geolocationLocal, 14));
        mapLabel_panel2.setIcon(mapGenerator.createLocaleMap(geolocationLocal, 14));
    }

    private void initLocalInformation() {

        try {
            localIpAddress = GeolocationOperations.getPcIpAddress();
            geolocationLocal = geolocationService.getJsonObject(localIpAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        localIpAddressLabel.setText(localIpAddress);
        localCountryLabel.setText(geolocationLocal.getCountry());
        localCityLabel.setText(geolocationLocal.getCity());
        localASLabel.setText(geolocationLocal.getAs());
        localOrgLabel.setText(geolocationLocal.getOrg());
    }
}
