package com.example.beimplementation.Config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class DatabaseConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        if (activeProfile.equals("prod")) {
            return null;
        } else {
            log.info("h2 server starts...");
            return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
        }
    }
}
