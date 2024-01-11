package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.VilageFcst;
import com.example.beimplementation.Exceptions.BadQueryException;
import com.example.beimplementation.Repositories.VilageFcstRepository;
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
public class VilageFcstService {

    private final VilageFcstRepository vilageFcstRepository;
    private final HttpRequestService httpRequestService;
    private final String endPoint = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    public List<VilageFcst> getVilageFcst(String serviceKey, int numOfRows, int pageNo, int baseDate,
                                          String baseTime, int nx, int ny) throws Exception {
        Pageable pageable = PageRequest.of(pageNo, numOfRows);

        if (vilageFcstRepository.countByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny) > 0) {

            return vilageFcstRepository.findAllByBaseDateAndBaseTimeAndNxAndNy(pageable, baseDate, baseTime, nx, ny).toList();

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

                LocalDateTime now = LocalDateTime.now();
                for (int i = 0; i < items.length(); i++) {
                    VilageFcst vilageFcst = new VilageFcst();

                    vilageFcst.setBaseDate(items.getJSONObject(i).getInt("baseDate"));
                    vilageFcst.setBaseTime(items.getJSONObject(i).getString("baseTime"));
                    vilageFcst.setNx(items.getJSONObject(i).getInt("nx"));
                    vilageFcst.setNy(items.getJSONObject(i).getInt("ny"));
                    vilageFcst.setCategory(items.getJSONObject(i).getString("category"));
                    vilageFcst.setFcstDate(items.getJSONObject(i).getInt("fcstDate"));
                    vilageFcst.setFcstTime(items.getJSONObject(i).getString("fcstTime"));
                    vilageFcst.setFcstValue(items.getJSONObject(i).getString("fcstValue"));
                    vilageFcst.setTimestamp(now);

                    vilageFcstRepository.save(vilageFcst);
                }
            }

            return vilageFcstRepository.findAllByBaseDateAndBaseTimeAndNxAndNy(pageable, baseDate, baseTime, nx, ny).toList();
        }
    };

    public int getCountByConditions(int baseDate, String baseTime, int nx, int ny) {
        return vilageFcstRepository.countByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny);
    }

    public LocalDateTime getLatestTime() {

        int[] openTime = {2, 5, 8, 11, 14, 17, 20, 23};

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int latestHour;

        if (hour < 2) {
            latestHour = 0;
        } else {
            latestHour = openTime[(hour-2)/3];
        }

        if (now.withHour(latestHour).withMinute(11).isBefore(now)) {
            return now.withHour(latestHour).withMinute(0);
        } else {
            return now.withHour(latestHour-3).withMinute(0);
        }

    }

}
