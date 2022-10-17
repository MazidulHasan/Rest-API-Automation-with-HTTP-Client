package com.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    public Properties prop;

    public int Response_Status_code_200 = 200;
    public int Response_Status_code_201 = 201;
    public int Response_Status_code_400 = 400;
    public int Response_Status_code_500 = 500;
    public int Response_Status_code_401 = 401;

    public TestBase(){
        prop = new Properties();
        try {
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/qa/configuration/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
