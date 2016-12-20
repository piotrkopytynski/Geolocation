package pl.geolocal.main;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

/**
 * Created by Piotr on 2016-12-20.
 */
@Component("mainClass")
public class MainClass {

    @PostConstruct
    void init(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow geo = new MainWindow();
                geo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                geo.pack();
                geo.setVisible(true);
            }
        });
    }
}