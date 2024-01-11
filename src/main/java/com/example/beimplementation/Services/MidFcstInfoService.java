package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.MidLandFcst;
import com.example.beimplementation.Exceptions.BadQueryException;
import com.example.beimplementation.Exceptions.MultipleEntryException;
import com.example.beimplementation.Repositories.MidLandFcstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MidFcstInfoService {

    private final String endPoint = "http://apis.data.go.kr/1360000/MidFcstInfoService";

    private final MidLandFcstRepository midLandFcstRepository;

    public MidLandFcst getMidLandFcst(String serviceKey, int numOfRows, int pageNo, String regId, String tmFc) throws Exception {
        MidLandFcst midLandFcst = null;

        List<MidLandFcst> listOfMidLandFcst = midLandFcstRepository.findAllByRegIdAndTmFc(regId, tmFc);

        if (listOfMidLandFcst.size() > 1) {
            log.error("multiple entries found");
            throw new MultipleEntryException(listOfMidLandFcst.size());
        } else if (listOfMidLandFcst.size() == 1) {
            midLandFcst = listOfMidLandFcst.get(0);
        } else {
            log.debug("cannot find entries");
            String fullEndPoint = endPoint +
                    "/getMidLandFcst" +
                    "?" + "ServiceKey=" + serviceKey +
                    "&" + "numOfRows=" + numOfRows +
                    "&" + "pageNo=" + pageNo +
                    "&" + "dataType=" + "JSON" +
                    "&" + "regId=" + regId +
                    "&" + "tmFc=" + tmFc;

            JSONObject jsonObject = getFromAPI(fullEndPoint).getJSONObject("response");

            JSONObject header = jsonObject.getJSONObject("header");

            if (!header.getString("resultMsg").equals("NORMAL_SERVICE")) {
                throw new BadQueryException(header.getString("resultCode"), header.getString("resultMsg"));
            }
            JSONObject body = jsonObject.getJSONObject("body");
            JSONObject item = body.getJSONObject("items").getJSONArray("item").getJSONObject(0);

            midLandFcst = new MidLandFcst();
            midLandFcst.setRegId(item.getString("regId"));
            midLandFcst.setTmFc(tmFc);

            midLandFcst.setRnSt3Am(item.getInt("rnSt3Am"));
            midLandFcst.setRnSt3Pm(item.getInt("rnSt3Pm"));
            midLandFcst.setRnSt4Am(item.getInt("rnSt4Am"));
            midLandFcst.setRnSt4Pm(item.getInt("rnSt4Pm"));
            midLandFcst.setRnSt5Am(item.getInt("rnSt5Am"));
            midLandFcst.setRnSt5Pm(item.getInt("rnSt5Pm"));
            midLandFcst.setRnSt6Am(item.getInt("rnSt6Am"));
            midLandFcst.setRnSt6Pm(item.getInt("rnSt6Pm"));
            midLandFcst.setRnSt7Am(item.getInt("rnSt7Am"));
            midLandFcst.setRnSt7Pm(item.getInt("rnSt7Pm"));

            midLandFcst.setRnSt8(item.getInt("rnSt8"));
            midLandFcst.setRnSt9(item.getInt("rnSt9"));
            midLandFcst.setRnSt10(item.getInt("rnSt10"));

            midLandFcst.setWf3Am(item.getString("wf3Am"));
            midLandFcst.setWf3Pm(item.getString("wf3Pm"));
            midLandFcst.setWf4Am(item.getString("wf4Am"));
            midLandFcst.setWf4Pm(item.getString("wf4Pm"));
            midLandFcst.setWf5Am(item.getString("wf5Am"));
            midLandFcst.setWf5Pm(item.getString("wf5Pm"));
            midLandFcst.setWf6Am(item.getString("wf6Am"));
            midLandFcst.setWf6Pm(item.getString("wf6Pm"));
            midLandFcst.setWf7Am(item.getString("wf7Am"));
            midLandFcst.setWf7Pm(item.getString("wf7Pm"));

            midLandFcst.setWf8(item.getString("wf8"));
            midLandFcst.setWf9(item.getString("wf9"));
            midLandFcst.setWf10(item.getString("wf10"));

            midLandFcst.setTimestamp(LocalDateTime.now());

            midLandFcstRepository.save(midLandFcst);
        }

        return midLandFcst;
    }

    private JSONObject getFromAPI(String fullEndPoint) throws Exception {
        URL url = new URL(fullEndPoint);

        log.debug("trying to connect - {}", fullEndPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try (AutoCloseable a = conn::disconnect) {

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);

            if (conn.getResponseCode() != 200) {
                log.error("getMidFcst job failed, status code: {}", conn.getResponseCode());
                throw new HttpClientErrorException(HttpStatus.valueOf(conn.getResponseCode()));

            } else {
                log.debug("getMidFcst connection successful, status code: {}", conn.getResponseCode());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();

                while (br.ready()) {
                    sb.append(br.readLine());
                }

                String responseString = sb.toString();
                log.debug(responseString);

                return new JSONObject(responseString);
            }
        }
    }

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
