package com.example.beimplementation.Utils;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseJsonBuilderTest {

    @Test
    void build() {

        ResponseJsonBuilder responseJsonBuilder = new ResponseJsonBuilder();

        //아무 정보 입력없이 빌드할 경우 오류코드를 포함하는지 확인
        JSONObject failedBuild = responseJsonBuilder.build();

        assertEquals("96", failedBuild.getJSONObject("response").getJSONObject("header").getString("resultCode"));
        assertEquals("FAILED_MAKING_RESPONSE", failedBuild.getJSONObject("response").getJSONObject("header").getString("resultMsg"));

        //빌드에 필요한 모든 자료가 주어질시 메타데이터 생성이 올바르게 되었는지 확인
        JSONObject successfulBuild;
        responseJsonBuilder = new ResponseJsonBuilder();
        responseJsonBuilder.setHeader("00", "NORMAL_SERVICE");
        responseJsonBuilder.setBody(1,1);
        responseJsonBuilder.appendItem(new JSONObject());

        successfulBuild = responseJsonBuilder.build();

        assertEquals("00", successfulBuild.getJSONObject("response").getJSONObject("header").getString("resultCode"));
        assertEquals("NORMAL_SERVICE", successfulBuild.getJSONObject("response").getJSONObject("header").getString("resultMsg"));
        assertEquals(1, successfulBuild.getJSONObject("response").getJSONObject("body").getInt("numOfRows"));
        assertEquals(1, successfulBuild.getJSONObject("response").getJSONObject("body").getInt("totalCount"));
        assertEquals(1, successfulBuild.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").length());
    }
}