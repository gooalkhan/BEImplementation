package com.example.beimplementation.Services;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class HttpRequestServiceTest {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    @InjectMocks
    private HttpRequestService httpRequestService;
    @InjectMocks
    private MidFcstInfoService midFcstInfoService;
    @InjectMocks
    private UltraSrtFcstService ultraSrtFcstService;
    @InjectMocks
    private VilageFcstService vilageFcstService;


    @Test
    void getFromAPI() {

        String[] endPoints = {
                "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?ServiceKey=" + apikey + "&numOfRows=1&pageNo=1&dataType=JSON&regId=11B00000&tmFc="+ midFcstInfoService.getLatestUpdateTime(),
                "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey="+ apikey +"&numOfRows=100&pageNo=1&base_date=" + vilageFcstService.getLatestTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + vilageFcstService.getLatestTime().format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=55&ny=127&dataType=JSON",
                "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=" + apikey + "&numOfRows=100&pageNo=1&base_date=" + ultraSrtFcstService.getLatestTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + vilageFcstService.getLatestTime().format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=55&ny=127&dataType=JSON"
        };

        for (String endPoint: endPoints) {
                assertDoesNotThrow(() -> {
                    JSONObject jsonObject = httpRequestService.getFromAPI(endPoint);
                    assertNotNull(jsonObject);
                });
        }

    }
}