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

    //외부에서 테스트 인메모리 데이터베이스인 H2를 접근하기 위한 서버 생성, 배포모드에서는 비활성됨
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
