package pl.geolocal.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Run{

    public static void main(String[] args) {

        new ClassPathXmlApplicationContext("spring-config.xml");

    }

}