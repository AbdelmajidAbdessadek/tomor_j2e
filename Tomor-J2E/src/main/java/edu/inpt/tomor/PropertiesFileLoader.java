package edu.inpt.tomor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileLoader {
    public static void main(String[] args) {
        Properties properties = new Properties();
        String userHome = System.getProperty("user.home");

        try (InputStream input = new FileInputStream(userHome + "/opt/config.properties")) {
            properties.load(input);

            String dbUrl = properties.getProperty("db.url");
            String dbUsername = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
