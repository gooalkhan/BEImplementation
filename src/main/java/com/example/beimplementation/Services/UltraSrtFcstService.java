package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.UltraSrtFcst;
import com.example.beimplementation.Exceptions.BadQueryException;
import com.example.beimplementation.Repositories.UltraSrtFcstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UltraSrtFcstService {

    private final UltraSrtFcstRepository ultraSrtFcstRepository;
    private final HttpRequestService httpRequestService;
    private final String endPoint = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    public List<UltraSrtFcst> getUltraSrtFcst(String serviceKey, int numOfRows, int pageNo, int baseDate,
                                              String baseTime, int nx, int ny) throws Exception {
        Pageable pageable = PageRequest.of(pageNo, numOfRows);

        if (ultraSrtFcstRepository.countByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny) > 0) {

            return ultraSrtFcstRepository.findAllByBaseDateAndBaseTimeAndNxAndNy(pageable, baseDate, baseTime, nx, ny).toList();

        } else {
            String fullEndPoint = endPoint +
                    "?" + "ServiceKey=" + serviceKey +
                    "&" + "numOfRows=" + "9999" +
                    "&" + "pageNo=" + "1" +
                    "&" + "dataType=" + "JSON" +
                    "&" + "base_date=" + baseDate +
                    "&" + "base_time=" + baseTime +
                    "&" + "nx=" + nx +
                    "&" + "ny=" + ny;

            JSONObject jsonObject = httpRequestService.getFromAPI(fullEndPoint);

            JSONObject header = jsonObject.getJSONObject("header");

            if (!header.getString("resultMsg").equals("NORMAL_SERVICE")) {
                throw new BadQueryException(header.getString("resultCode"), header.getString("resultMsg"));
            } else {
                JSONObject body = jsonObject.getJSONObject("body");
                JSONArray items = body.getJSONObject("items").getJSONArray("item");

                UltraSrtFcst ultraSrtFcst;
                LocalDateTime now = LocalDateTime.now();
                for (int i = 0; i < items.length(); i++) {
                    ultraSrtFcst = new UltraSrtFcst();

                    ultraSrtFcst.setBaseDate(items.getJSONObject(i).getInt("baseDate"));
                    ultraSrtFcst.setBaseTime(items.getJSONObject(i).getString("baseTime"));
                    ultraSrtFcst.setNx(items.getJSONObject(i).getInt("nx"));
                    ultraSrtFcst.setNy(items.getJSONObject(i).getInt("ny"));
                    ultraSrtFcst.setCategory(items.getJSONObject(i).getString("category"));
                    ultraSrtFcst.setFcstDate(items.getJSONObject(i).getInt("fcstDate"));
                    ultraSrtFcst.setFcstTime(items.getJSONObject(i).getString("fcstTime"));
                    ultraSrtFcst.setFcstValue(items.getJSONObject(i).getString("fcstValue"));
                    ultraSrtFcst.setTimestamp(now);

                    ultraSrtFcstRepository.save(ultraSrtFcst);
                }
            }

            return ultraSrtFcstRepository.findAllByBaseDateAndBaseTimeAndNxAndNy(pageable, baseDate, baseTime, nx, ny).toList();
        }
    };

    public int getCountByConditions(int baseDate, String baseTime, int nx, int ny) {
        return ultraSrtFcstRepository.countByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny);
    }

    public LocalDateTime getLatestTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime readyTime = now.withMinute(46);
        LocalDateTime result;

        if (now.isAfter(readyTime)) {
            result = now.withMinute(30);

        } else {
            result = now.withMinute(30).minusHours(1);
        }
        return result;
    }

}
