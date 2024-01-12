package com.example.beimplementation.Controllers;

import com.example.beimplementation.Services.VilageFcstService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VilageFcstInfoControllerTest {

    @Autowired
    VilageFcstInfoController vilageFcstInfoController;

    @Autowired
    VilageFcstService vilageFcstService;

    @Test
    void getVilageFcst() {

        LocalDateTime latestTime = vilageFcstService.getLatestTime();
        int baseDate = Integer.parseInt(latestTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        String baseTime = latestTime.format(DateTimeFormatter.ofPattern("HHmm"));

        String result = vilageFcstInfoController.getVilageFcst(10, 1, baseDate, baseTime, 55, 127);

        //요청시 예외발생없이 json형식으로 반환되는지 확인하고, 반환되는 자료의 개수가 메타데이터와 동일한지 확인
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