package pl.geolocal.main;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.geolocal.domain.impl.GeolocationImpl;
import pl.geolocal.service.GeolocationService;
import pl.geolocal.service.impl.GeolocationServiceImpl;
import pl.geolocal.util.GeolocationOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/*
 * Created by JFormDesigner on Sat Dec 17 16:27:38 CET 2016
 */


/**
 * @author Piotr Kopytynski
 */
@Getter
@Setter
@Component("mainWindow")
public class MainWindow extends JFrame {

    //    @Resource(name="geolocationService")
//    GeolocationServiceImpl geolocationService;

    GeolocationService geolocationService = new GeolocationServiceImpl();

    public MainWindow() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Piotr Kopytynski
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        label1_pan1 = new JLabel();
        ipAddressInput1_pan1 = new JTextField();
        button1_pan1 = new JButton();
        panel2 = new JPanel();
        label1_pan2 = new JLabel();
        ipAddressInput1_pan2 = new JTextField();
        label2_pan2 = new JLabel();
        ipAddressInput2_pan2 = new JTextField();
        button1_pan2 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "2*(default, $lcgap), 16dlu, $lcgap, 70dlu, $lcgap, 115dlu, $lcgap, 42dlu, 2*($lcgap, default)",
                "5*(default, $lgap), 73dlu, 2*($lgap, default)"));

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                // JFormDesigner evaluation mark
                panel1.setBorder(new javax.swing.border.CompoundBorder(
                        new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                                "", javax.swing.border.TitledBorder.CENTER,
                                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                                java.awt.Color.red), panel1.getBorder()));
                panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    public void propertyChange(java.beans.PropertyChangeEvent e) {
                        if ("border".equals(e.getPropertyName())) throw new RuntimeException();
                    }
                });

                panel1.setLayout(new FormLayout(
                        "26dlu, $lcgap, 56dlu, $lcgap, 127dlu, $lcgap, 44dlu, $lcgap, 62dlu",
                        "4*(default, $lgap), default"));

                //---- label1_pan1 ----
                label1_pan1.setText("Remote PC IP:");
                panel1.add(label1_pan1, CC.xy(3, 5));
                panel1.add(ipAddressInput1_pan1, CC.xy(5, 5));

                //---- button1_pan1 ----
                button1_pan1.setText("Calculate distance");
                panel1.add(button1_pan1, CC.xy(5, 9));
            }
            tabbedPane1.addTab("Your PC", panel1);

            //======== panel2 ========
            {
                panel2.setLayout(new FormLayout(
                        "26dlu, $lcgap, 58dlu, $lcgap, 129dlu, $lcgap, default",
                        "4*(default, $lgap), default"));

                //---- label1_pan2 ----
                label1_pan2.setText("IP address 1:");
                panel2.add(label1_pan2, CC.xy(3, 5, CC.LEFT, CC.DEFAULT));
                panel2.add(ipAddressInput1_pan2, CC.xy(5, 5));

                //---- label2_pan2 ----
                label2_pan2.setText("IP address 2:");
                panel2.add(label2_pan2, CC.xy(3, 7));
                panel2.add(ipAddressInput2_pan2, CC.xy(5, 7));

                //---- button1_pan2 ----
                button1_pan2.setText("Calculate distance");
                panel2.add(button1_pan2, CC.xy(5, 9));
            }
            tabbedPane1.addTab("Remote PCs", panel2);
        }
        contentPane.add(tabbedPane1, CC.xywh(1, 1, 9, 11));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

//        LISTENERS

        button1_pan1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String remoteIpAddress = ipAddressInput1_pan1.getText();
                    String localIpAddress = GeolocationOperations.getPcIpAddress();
                    GeolocationImpl geolocationLocal = geolocationService.getJsonObject(localIpAddress);
                    GeolocationImpl geolocationRemote = geolocationService.getJsonObject(remoteIpAddress);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        button1_pan2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String remoteIpAddres1 = ipAddressInput1_pan2.getText();
                String remoteIpAddres2 = ipAddressInput2_pan2.getText();
                try {
                    GeolocationImpl geolocationRemote1 = geolocationService.getJsonObject(remoteIpAddres1);
                    GeolocationImpl geolocationRemote2 = geolocationService.getJsonObject(remoteIpAddres2);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Piotr Kopytynski
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JLabel label1_pan1;
    private JTextField ipAddressInput1_pan1;
    private JButton button1_pan1;
    private JPanel panel2;
    private JLabel label1_pan2;
    private JTextField ipAddressInput1_pan2;
    private JLabel label2_pan2;
    private JTextField ipAddressInput2_pan2;
    private JButton button1_pan2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
