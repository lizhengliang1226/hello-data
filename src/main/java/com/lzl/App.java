package com.lzl;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.log.Log;
import cn.hutool.setting.Setting;
import com.lzl.datagenerator.DataGenerator;
import com.lzl.datagenerator.config.Configuration;
import com.lzl.datagenerator.entity.GenColumnConfig;
import com.lzl.datagenerator.entity.GenSystemConfig;

import java.sql.SQLException;
import java.util.List;


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