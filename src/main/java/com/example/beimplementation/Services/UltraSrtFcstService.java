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
        //기본 다중조회라 페이징으로 검색가능

        if (ultraSrtFcstRepository.countByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny) > 0) {

            return ultraSrtFcstRepository.findAllByBaseDateAndBaseTimeAndNxAndNy(pageable, baseDate, baseTime, nx, ny).toList();

        } else {
            String fullEndPoint = endPoint +
                    "?" + "ServiceKey=" + serviceKey +
                    "&" + "numOfRows=" + "9999" + //최대 가져올 수 있는 건수가 9999건, 모두 가져와서 일부만 페이징으로 리턴
                    "&" + "pageNo=" + "1" +
                    "&" + "dataType=" + "JSON" +
                    "&" + "base_date=" + baseDate +
                    "&" + "base_time=" + baseTime +
                    "&" + "nx=" + nx +
                    "&" + "ny=" + ny;

            JSONObject jsonObject = httpRequestService.getFromAPI(fullEndPoint);

            JSONObject header = jsonObject.getJSONObject("header");

            if (!header.getString("resultMsg").equals("NORMAL_SERVICE")) {
                log.error("client gives wrong parameter");
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

    //페이징을 하면 갖고온 건수 대비 전체건수를 보여주기 위해 전체건수 가져오는 부분
    public int getCountByConditions(int baseDate, String baseTime, int nx, int ny) {
        return ultraSrtFcstRepository.countByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny);
    }

    //기상청 발표시간 자동생성, API마다 발표시간이 달라 별도로 구현
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
