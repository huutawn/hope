package com.llt.hope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.llt.hope.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.llt.hope.repository.elasticsearch")
public class HopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HopeApplication.class, args);
    }
}
