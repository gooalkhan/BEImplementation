package com.example.beimplementation.Controllers;

import com.example.beimplementation.Services.UltraSrtFcstService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UltraSrtInfoControllerTest {

    @Autowired
    UltraSrtInfoController ultraSrtInfoController;

    @Autowired
    UltraSrtFcstService ultraSrtFcstService;

    @Test
    void getUltraSrtFcst() {

        LocalDateTime latestTime = ultraSrtFcstService.getLatestTime();
        int baseDate = Integer.parseInt(latestTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        String baseTime = latestTime.format(DateTimeFormatter.ofPattern("HHmm"));

        String result = ultraSrtInfoController.getUltraSrtFcst(10, 1, baseDate, baseTime, 55, 127);

        assertDoesNotThrow(() -> {
            JSONObject jsonObject = new JSONObject(result);
            String resultCode = jsonObject.getJSONObject("response").getJSONObject("header").getString("resultCode");
            assertEquals(resultCode, "00");
            int itemLength = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").length();
            int numOfRows = jsonObject.getJSONObject("response").getJSONObject("body").getInt("numOfRows");

            assertEquals(itemLength, numOfRows);
        });

    }
}