package com.sparta.write;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing//h2설정후 추가로 설정
@SpringBootApplication
public class WriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(WriteApplication.class, args);
    }

}
