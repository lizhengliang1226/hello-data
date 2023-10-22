package com.lzl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzl.datagenerator.DataGenerator;
import com.lzl.datagenerator.entity.GenStrategyTemplate;
import com.lzl.datagenerator.loader.ConfigLoader;
import com.lzl.datagenerator.mapper.HelloMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Hello world!
 *
 * @author lzl1226
 */
@RestController
@RequestMapping("service")
@SpringBootApplication()
@EnableConfigurationProperties
@MapperScan("com.lzl.datagenerator.mapper.**")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
        //        new App().start();
    }
    @Autowired
    private HelloMapper helloMapper;
    void start() {
        // Configuration instance = Configuration.getInstance();
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.load();
        DataGenerator dataGenerator = new DataGenerator(configLoader);
        dataGenerator.generate();
        configLoader.destory();
    }

    @GetMapping("strategyTmpl")
    public List<GenStrategyTemplate> queryStrategyTemplate(){
        LambdaQueryWrapper<GenStrategyTemplate> wrapper=new LambdaQueryWrapper<>();
        List<GenStrategyTemplate> genStrategyTemplates = helloMapper.selectList(wrapper);
        return genStrategyTemplates;
    }

}