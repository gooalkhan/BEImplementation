package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.VilageFcst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VilageFcstServiceTest {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    @Autowired
    private VilageFcstService vilageFcstService;

    @Test
    void getVilageFcst() {

        LocalDateTime latestTime = vilageFcstService.getLatestTime();

        int baseDate = Integer.parseInt(latestTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        String baseTime = latestTime.format(DateTimeFormatter.ofPattern("HHmm"));

        assertDoesNotThrow(() -> {
            List<VilageFcst> vilageFcsts = vilageFcstService.getVilageFcst(apikey, 1, 1, baseDate, baseTime, 55, 127);
            assertNotNull(vilageFcsts.get(0));
        });

    }

    @Test
    void getLatestTime() {
        LocalDateTime latestTime = vilageFcstService.getLatestTime();
        assertNotNull(latestTime);
        assertEquals(latestTime.getDayOfMonth(), LocalDateTime.now().getDayOfMonth());
    }
}