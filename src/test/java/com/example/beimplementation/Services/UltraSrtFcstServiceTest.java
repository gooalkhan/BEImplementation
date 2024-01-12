package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.UltraSrtFcst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UltraSrtFcstServiceTest {

    @Autowired
    UltraSrtFcstService ultraSrtFcstService;

    @Value("${openapi.midfcst.key}")
    private String apikey;

    @Test
    void getUltraSrtFcst() {

        LocalDateTime latestTime = ultraSrtFcstService.getLatestTime();

        int baseDate = Integer.parseInt(latestTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        String baseTime = latestTime.format(DateTimeFormatter.ofPattern("HHmm"));

        assertDoesNotThrow(() -> {
            List<UltraSrtFcst> ultraSrtFcst = ultraSrtFcstService.getUltraSrtFcst(apikey, 1, 1, baseDate, baseTime, 55, 127);
            assertNotNull(ultraSrtFcst.get(0));
        });

    }

    @Test
    void getLatestTime() {
        LocalDateTime latestTime = ultraSrtFcstService.getLatestTime();
        assertNotNull(latestTime);
        assertEquals(latestTime.getDayOfMonth(), LocalDateTime.now().getDayOfMonth());
    }
}