package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.UltraSrtFcst;
import com.example.beimplementation.Repositories.UltraSrtFcstRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UltraSrtFcstService {

    private final UltraSrtFcstRepository ultraSrtFcstRepository;
    private final HttpRequestService httpRequestService;
    private final String endPoint = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    public List<UltraSrtFcst> getUltraSrtFcst(String serviceKey, int numOfRows, int pageNo, int baseDate,
                                        String baseTime, int nx, int ny) {

        List<UltraSrtFcst> listOfUltraSrtFcst = ultraSrtFcstRepository.findAllByBaseDateAndBaseTimeAndNxAndNy(baseDate, baseTime, nx, ny);

        if (!listOfUltraSrtFcst.isEmpty()) {
            return listOfUltraSrtFcst;
        } else {
            String fullEndPoint = endPoint +
                    "?" + "ServiceKey=" + serviceKey +
                    "&" + "numOfRows=" + "9999" +
                    "&" + "pageNo=" + "1" +
                    "&" + "dataType=" + "JSON" +


        }

    };

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
