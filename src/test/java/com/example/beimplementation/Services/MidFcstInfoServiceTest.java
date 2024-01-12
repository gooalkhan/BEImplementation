package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.MidLandFcst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MidFcstInfoServiceTest {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    @Autowired
    private MidFcstInfoService midFcstInfoService;

    @Test
    void getMidLandFcst() {
        String latestTime = midFcstInfoService.getLatestUpdateTime();
        assertNotNull(latestTime);

        assertDoesNotThrow(() -> {
            MidLandFcst midLandFcst = midFcstInfoService.getMidLandFcst(apikey, 1, 1, "11B00000", latestTime);
            assertNotNull(midLandFcst);
        });

    }

    @Test
    void getLatestUpdateTime() {

        String latestTime = midFcstInfoService.getLatestUpdateTime();

        assertNotNull(latestTime);

        assertDoesNotThrow(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDateTime dateTime = LocalDateTime.parse(latestTime, formatter);
        });


    }
}