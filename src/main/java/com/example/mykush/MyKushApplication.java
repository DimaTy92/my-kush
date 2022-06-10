package com.example.mykush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyKushApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyKushApplication.class, args);
    }

}
