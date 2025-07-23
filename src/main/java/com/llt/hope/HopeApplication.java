package com.llt.hope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HopeApplication.class, args);
    }
}
