package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.MidFcst;
import com.example.beimplementation.Repositories.MidFcstRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MidFcstInfoServiceTest {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    @Autowired
    private MidFcstInfoService midFcstInfoService;

    @Autowired
    private MidFcstRepository midFcstRepository;

    private final String latestUpdateTime = midFcstInfoService.getLatestUpdateTime();

    @Test
    public void checkIfMidFcstIsNotNull() {
        try {
            MidFcst midFcst = midFcstInfoService.getMidFcstFromAPI(apikey, 1, 1, 108, latestUpdateTime);
            assertNotNull(midFcst);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}