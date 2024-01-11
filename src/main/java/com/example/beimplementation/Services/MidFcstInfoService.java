package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.MidFcst;
import com.example.beimplementation.Repositories.MidFcstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service
public class MidFcstInfoService {

    private final String endPoint = "http://apis.data.go.kr/1360000/MidFcstInfoService";

    private final MidFcstRepository midFcstRepository;

    public MidFcst getMidFcst(String serviceKey, int numOfRows, int pageNo, String dataType, int stnId, String tmFc) throws Exception {
        MidFcst midFcst = null;

        List<MidFcst> listOfMidFcst =  midFcstRepository.findAllByNumOfRowsAndPageNoAndDataTypeAndStnIdAndTmFc(numOfRows, pageNo, dataType,  stnId, tmFc);

        if (listOfMidFcst.size() > 1) {
            //TODO null return이 아니라 커스텀 예외를 던지게 하기

        } else if (listOfMidFcst.size() == 1) {
            midFcst = listOfMidFcst.get(0);
        } else {
            midFcst = getMidFcstFromAPI(serviceKey, numOfRows, pageNo, stnId, tmFc);

            if (midFcst != null) {
                midFcstRepository.save(midFcst);
            }
        }

        return midFcst;
    }

    public MidFcst getMidFcstFromAPI(String serviceKey, int numOfRows, int pageNo, int stnId, String tmFc) throws Exception {
        MidFcst midFcst = null;

        String fullEndPoint = endPoint +
                "/getMidFcst" +
                "?" + "ServiceKey=" + serviceKey +
                "&" + "numOfRows=" + numOfRows +
                "&" + "pageNo=" + pageNo +
                "&" + "dataType=" + "JSON" +
                "&" + "stnId=" + stnId +
//                "&" + "tmFc=" + tmFc.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
                "&" + "tmFc=" + tmFc;

        URL url = new URL(fullEndPoint);

        log.debug("trying to connect - {}", fullEndPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try (AutoCloseable a = conn::disconnect) {

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);

            if (conn.getResponseCode() != 200) {
                log.error("getMidFcst job failed, status code: {}", conn.getResponseCode());
                return midFcst;

            } else {
                log.debug("getMidFcst connection successful, status code: {}", conn.getResponseCode());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();

                while(br.ready()) {
                    sb.append(br.readLine());
                }

                String responseString = sb.toString();
                log.debug(responseString);

                JSONObject jsonObject = new JSONObject(responseString).getJSONObject("response");
                JSONObject header = jsonObject.getJSONObject("header");
                JSONObject body = jsonObject.getJSONObject("body");
                JSONArray items = body.getJSONObject("items").getJSONArray("item");

                if (!header.getString("resultMsg").equals("NORMAL_SERVICE")) {
                    return midFcst;
                }

                midFcst = new MidFcst();

                midFcst.setNumOfRows(body.getInt("numOfRows"));
                midFcst.setPageNo(body.getInt("pageNo"));
                midFcst.setTotalCount(body.getInt("totalCount"));
                midFcst.setResultCode(header.getInt("resultCode"));
                midFcst.setResultMsg(header.getString("resultMsg"));
                midFcst.setDataType(body.getString("dataType"));
                midFcst.setTmFc(tmFc);
                midFcst.setStnId(stnId);
                midFcst.setWfSv(items.getJSONObject(0).getString("wfSv"));
                midFcst.setTimeStamp(LocalDateTime.now());
            }

        };
        return midFcst;
    };

    public String getLatestUpdateTime() {
        LocalDateTime result;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime atSix = LocalDateTime.now().with(LocalTime.of(6,0));
        LocalDateTime atEightTeen = LocalDateTime.now().with(LocalTime.of(18,0));

        if (now.isBefore(atSix)) {
            result = atEightTeen.minusDays(1);
        } else if (now.isEqual(atEightTeen) || now.isAfter(atEightTeen)) {
            result = atEightTeen;
        } else {
        //else if (now.isEqual(atSix) || (now.isAfter(atSix) & now.isBefore(atEightTeen))) {
        result = atSix;}
        //}
        return result.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }
}
