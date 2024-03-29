package com.example.beimplementation.Controllers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MidFcstInfoControllerTest {

    @Autowired
    MidFcstInfoController midFcstInfoController;

    @Test
    void getMidLandFcst() {

        String result = midFcstInfoController.getMidLandFcst("1","1","11B00000","");

        //요청시 예외발생없이 json 형태로 반환되며, 정상코드인지 확인
        assertDoesNotThrow(() -> {
            JSONObject jsonObject = new JSONObject(result);
            String resultCode = jsonObject.getJSONObject("response").getJSONObject("header").getString("resultCode");
            assertEquals(resultCode, "00");
        });

    }
}