package com.navigation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.navigation.mapper")
@SpringBootApplication
public class NavigationApplication {

    public static void main(String[] args) {

        SpringApplication.run(NavigationApplication.class,args);

    }

}