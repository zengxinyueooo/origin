package com.navigation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.navigation.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NavigationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NavigationApplication.class,args);
    }
}
