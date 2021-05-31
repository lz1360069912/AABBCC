package com.company.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaServer
@Slf4j
public class eurekaApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(eurekaApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("启动成功");
        log.info("Eureka地址:\thttp://127.0.0.1:{}",env.getProperty("server.port"));
    }
}
