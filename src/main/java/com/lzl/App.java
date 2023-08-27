package com.lzl;

import com.lzl.datagenerator.DataGenerator;
import com.lzl.datagenerator.config.Configuration;


/**
 * Hello world!
 *
 * @author lzl1226
 */
public class App {
    public static void main(String[] args) {
        new App().start();
    }

    void start() {
        Configuration instance = Configuration.getInstance();
        DataGenerator dataGenerator = new DataGenerator(instance);
        dataGenerator.generate();
    }
}