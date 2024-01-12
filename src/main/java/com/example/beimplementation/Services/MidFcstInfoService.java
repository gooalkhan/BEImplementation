package com.example.beimplementation.Services;

import com.example.beimplementation.Entities.MidLandFcst;
import com.example.beimplementation.Exceptions.BadQueryException;
import com.example.beimplementation.Exceptions.MultipleEntryException;
import com.example.beimplementation.Repositories.MidLandFcstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service
public class MidFcstInfoService {

    private final String endPoint = "https://apis.data.go.kr/1360000/MidFcstInfoService";

    private final MidLandFcstRepository midLandFcstRepository;
    private final HttpRequestService httpRequestService;

    //데이터베이스에 자료있으면 바로 리턴, 없으면 요청 후 데이터베이스에 저장하고 리턴
    public MidLandFcst getMidLandFcst(String serviceKey, int numOfRows, int pageNo, String regId, String tmFc) throws Exception {
        MidLandFcst midLandFcst = null;

        List<MidLandFcst> listOfMidLandFcst = midLandFcstRepository.findAllByRegIdAndTmFc(regId, tmFc);

        if (listOfMidLandFcst.size() > 1) {
            log.error("multiple entries found");
            throw new MultipleEntryException(listOfMidLandFcst.size());
        } else if (listOfMidLandFcst.size() == 1) {
            //동일조건이면 한개만 검색되어야함
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

            JSONObject jsonObject = httpRequestService.getFromAPI(fullEndPoint);

            JSONObject header = jsonObject.getJSONObject("header");

            if (!header.getString("resultMsg").equals("NORMAL_SERVICE")) {
                log.error("client gives wrong parameter");
                throw new BadQueryException(header.getString("resultCode"), header.getString("resultMsg"));
            }
            JSONObject body = jsonObject.getJSONObject("body");
            JSONObject item = body.getJSONObject("items").getJSONArray("item").getJSONObject(0);

            //json파싱 후 엔티티 객체 생성
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

    // 기상청 API 발표 시간을 구해주는 부분, API마다 발표시간이 달라 분리하여 구현
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
