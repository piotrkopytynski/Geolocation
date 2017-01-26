package pl.geolocal.main;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.geolocal.domain.impl.Geolocation;
import pl.geolocal.domain.impl.TableRow;
import pl.geolocal.service.DistanceService;
import pl.geolocal.service.GeolocationService;
import pl.geolocal.service.PingService;
import pl.geolocal.util.GeolocationOperations;
import pl.geolocal.util.IpValidator;
import pl.geolocal.util.MapGenerator;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private final PingService pingService;

    List<TableRow> tableRows = new ArrayList<>();

    private String localIpAddress;
    private Geolocation geolocationLocal;

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
    private JLabel rttValueLabel;
    private JButton uploadFileButton;
    private JTable table1;
    private JProgressBar progressBar1;
    private JButton startButton;
    private JButton saveResultsButton;
    private JComboBox comboBox1;
    private JButton clearButton;
    private JPanel dataPanel;
    private JLabel ipPathLabel;
    private JLabel mapLabel;


    @Autowired
    public MainWindow(GeolocationService geolocationService, DistanceService distanceService, IpValidator ipValidator,
                      MapGenerator mapGenerator, PingService pingService) {
        this.geolocationService = geolocationService;
        this.distanceService = distanceService;
        this.ipValidator = ipValidator;
        this.mapGenerator = mapGenerator;
        this.pingService = pingService;
    }

    @PostConstruct
    public void init() {
        initComponents();
    }

    private void initComponents() {
        setResizable(false);
        initLocalInformation();
        initDefaultMaps();
        initializeTable();
        initializeComboBox();
        initializeListeners();
    }

    private void initializeListeners() {
        calculateButton_panel1.addActionListener(e -> {
            setStateInformation("Waiting...");
            new Thread(this::calculateLocalAction).start();
            new Thread(this::pingRemoteAction).start();
        });

        calculateButton_panel2.addActionListener(e -> {
            calculateButton_panel2.setEnabled(false);
            new Thread(this::calculateRemoteAction).start();
        });

        uploadFileButton.addActionListener(e -> {
            uploadFileButton.setEnabled(false);
            progressBar1.setValue(0);
            new Thread(this::uploadFile).start();
        });

        startButton.addActionListener(e -> {
            progressBar1.setValue(0);
            new Thread(this::calculateTable).start();
        });

        clearButton.addActionListener(e -> {
            clearTable();
        });

        saveResultsButton.addActionListener(e -> {
            generateFileWithData();
            JOptionPane.showMessageDialog(rootPanel, "Generating completed");
        });
    }

    private void generateFileWithData() {
        File file = new File("data.txt");
        StringBuilder stringBuilder = new StringBuilder();
        tableRows.forEach(tableRow -> {
            tableRow.getRttMeasurement().forEach((date, aDouble) -> {
                stringBuilder.append(tableRow.toStringBuilder().append(",")
                        .append(aDouble)).append("\n");
            });
        });
        try {
            FileUtils.writeStringToFile(file, String.valueOf(stringBuilder));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        tableRows = new ArrayList<>();
        progressBar1.setValue(0);
    }

    private void initializeComboBox() {
        for (int i = 1; i <= 1000; i++) {
            comboBox1.addItem(i);
        }
    }

    private void calculateTable() {
        startButton.setEnabled(false);
        uploadFileButton.setEnabled(false);
        comboBox1.setEnabled(false);
        clearButton.setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        int index = 1;
        do {
            int finalIndex = index;
            tableRows.forEach(tableRow -> {
                try {
                    Map<Date, Double> rttMeasurement = tableRow.getRttMeasurement();
                    rttMeasurement.put(new Date(), pingService.calculateRttValue(tableRow.getIpAddress()));
                    Median median1 = new Median();
                    if(finalIndex > 1) {
                        model.removeRow(tableRows.indexOf(tableRow));
                    }
                    model.insertRow(tableRows.indexOf(tableRow) ,new Object[]{
                            finalIndex,
                            dateFormat.format(rttMeasurement.keySet().toArray()[rttMeasurement.size() - 1]),
                            tableRow.getIpAddress(),
                            tableRow.getCountry(),
                            tableRow.getCity(), tableRow.getDistance(),
                            rttMeasurement.values().toArray()[rttMeasurement.size() - 1],
                            median1.evaluate(rttMeasurement.values().stream().mapToDouble(Double::doubleValue).toArray())});
                    progressBar1.setValue((int)Math.ceil(progressBar1.getValue() +
                            (100.0 / ((tableRows.size()) * ((Integer) comboBox1.getSelectedItem())))));

                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            });
            index++;
        } while(((int)comboBox1.getSelectedItem() >= index));
        startButton.setEnabled(true);
        uploadFileButton.setEnabled(true);
        comboBox1.setEnabled(true);
        clearButton.setEnabled(true);
        saveResultsButton.setEnabled(true);
        JOptionPane.showMessageDialog(rootPanel, "Calculating completed");
    }

    private void uploadFile() {
        StringTokenizer data = null;
        File file = openFile();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        if (file != null) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                String everything = IOUtils.toString(inputStream);
                data = new StringTokenizer(everything, ",");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            int i = data.countTokens();

            int index = 0;
            while (data.hasMoreTokens()) {
                TableRow tableRow = new TableRow((data.nextToken()));

                progressBar1.setValue(progressBar1.getValue() + (100 / i));
                Geolocation object = null;
                try {
                    object = geolocationService.getJsonObject(tableRow.getIpAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tableRow.setCity(object.getCity());
                tableRow.setCountry(object.getCountry());
                tableRow.setDistance(Long.valueOf(distanceService.calculate(geolocationLocal, object).replaceAll(" km", "")));
                tableRows.add(tableRow);
                model.addRow(new Object[]{index ,"-",tableRow.getIpAddress(), tableRow.getCountry(),
                        tableRow.getCity(), tableRow.getDistance(), "-","-"});
                index++;
            }
            uploadFileButton.setEnabled(true);
        }
    }

    private void initializeTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Number");
        model.addColumn("Date and time");
        model.addColumn("IP address");
        model.addColumn("Country");
        model.addColumn("City");
        model.addColumn("Distance");
        model.addColumn("Last RTT");
        model.addColumn("RTT median");
        table1.setModel(model);
    }

    private File openFile() {
        JFileChooser openFile = new JFileChooser();
        int dialog = openFile.showOpenDialog(null);
        if (dialog == JFileChooser.APPROVE_OPTION) {
            return openFile.getSelectedFile();
        }
        return null;
    }

    private void setStateInformation(String status) {
        distanceLabel_panel1.setText(status);
        rttValueLabel.setText(status);
        ipPathLabel.setText(status);
        calculateButton_panel1.setEnabled(false);
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
        calculateButton_panel2.setEnabled(true);
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

    private void pingRemoteAction() {
        String remoteIpAddress = remoteIpAddressTextField.getText();
        Geolocation geolocationRemote = null;
        if (!ipValidator.validate(rootPanel, remoteIpAddress)) {
            try {
                geolocationRemote = geolocationService.getJsonObject(remoteIpAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ipValidator.validateExistence(rootPanel, geolocationRemote, remoteIpAddress)) {
                Double rttValue;
                Integer ipPathValue;

                if ((rttValue = pingService.calculateRttValue(remoteIpAddress)) != null) {
                    rttValueLabel.setText(String.valueOf(rttValue) + " ms");
                } else {
                    rttValueLabel.setText("Request timed out");
                }

                if ((ipPathValue = pingService.tracerouteRemote(remoteIpAddress)) > 0){
                    ipPathLabel.setText(String.valueOf((ipPathValue)));
                } else {
                    ipPathLabel.setText("Unable to perform traceroute");
                }
            }
        }
        calculateButton_panel1.setEnabled(true);
    }

    private void calculateLocalAction() {
        distanceLabel_panel1.setText("Waiting");
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
