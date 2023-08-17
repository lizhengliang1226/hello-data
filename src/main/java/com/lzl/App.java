package com.lzl;

import com.lzl.datagenerator.DataGenerator;
import com.lzl.datagenerator.config.Configuration;

/**
 * Hello world!
 * @author lzl1226
 */
public class App {
    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator(Configuration.getInstance());
        dataGenerator.generate();
    }
}