package pl.geolocal.main;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

/**
 * Created by Piotr on 2016-12-20.
 */
@Component
public class MainClass {

    private final MainWindow mainWindow;

    @Autowired
    public MainClass(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @PostConstruct
    void init(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainWindow.setContentPane(mainWindow.getRootPanel());
                mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainWindow.pack();
                mainWindow.setVisible(true);
            }
        });
    }
}