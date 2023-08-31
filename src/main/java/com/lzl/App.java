package com.lzl;

import com.lzl.datagenerator.DataGenerator;
import com.lzl.datagenerator.loader.ConfigLoader;



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
        // Configuration instance = Configuration.getInstance();
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.load();
        DataGenerator dataGenerator = new DataGenerator(configLoader);
        dataGenerator.generate();
        configLoader.destory();
    }


}