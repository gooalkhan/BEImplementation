package com.example.beimplementation.Controllers;

import com.example.beimplementation.Entities.UltraSrtFcst;
import com.example.beimplementation.Exceptions.BadQueryException;
import com.example.beimplementation.Services.UltraSrtFcstService;
import com.example.beimplementation.Utils.ResponseJsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//초단기예보 콘트롤러

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/UltraSrtFcst")
@RestController
public class UltraSrtInfoController {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    private final UltraSrtFcstService ultraSrtFcstService;

    @GetMapping(value = "/getUltraSrtFcst", produces = "application/json")
    public String getUltraSrtFcst(@RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
                                  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                  @RequestParam(value = "baseDate", defaultValue = "999999") int baseDate,
                                  @RequestParam(value = "baseTime", defaultValue = "") String baseTime,
                                  @RequestParam(value = "nx", defaultValue = "0") int nx,
                                  @RequestParam(value = "ny", defaultValue = "0") int ny
    ) {
        ResponseJsonBuilder jsonBuilder = new ResponseJsonBuilder();
        String response;

        if (nx == 0 || ny == 0) {
            //필수입력값인 지역위치 미입력시 에러코드 리턴
            log.error("client gives no required parameter");
            jsonBuilder.setHeader("97", "NO_PARAMETER");
            jsonBuilder.setBody(0, 0);
            return jsonBuilder.build().toString();
        } else {
            //필수입력값인 발표시각 미입력시 자동으로 계산
            if (baseDate == 999999 || baseTime.isEmpty()) {
                LocalDateTime latestTime = ultraSrtFcstService.getLatestTime();
                baseDate = Integer.parseInt(latestTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                baseTime = latestTime.format(DateTimeFormatter.ofPattern("HHmm"));
            }

            try {
                //정상진행시
                List<UltraSrtFcst> listOfUltraSrtFcst = ultraSrtFcstService.getUltraSrtFcst(
                        apikey, numOfRows, pageNo, baseDate, baseTime, nx, ny);
                jsonBuilder.setHeader("00", "NORMAL_SERVICE");
                jsonBuilder.setBody(listOfUltraSrtFcst.size(), ultraSrtFcstService.getCountByConditions(baseDate, baseTime, nx, ny));
                for (UltraSrtFcst item:listOfUltraSrtFcst) {
                    jsonBuilder.appendItem(new JSONObject(item));
                }

            } catch (BadQueryException bqe) {
                //입력값에 문제발생시 예외처리
                log.error("bad query");
                jsonBuilder.setHeader(bqe.getResultCode(), bqe.getResultMsg());
                jsonBuilder.setBody(0, 0);

            } catch (HttpClientErrorException hee) {
                //기상청에 연결안될시 예외
                log.error("cannot connect to origin - {}", hee.getStatusCode());
                jsonBuilder.setHeader("99", "CONNECTION_ERROR");
                jsonBuilder.setBody(0, 0);

            } catch (Exception e) {
                //기타예외
                log.error(e.getMessage());
                jsonBuilder.setHeader("98", "SERVER_ERROR");
                jsonBuilder.setBody(0, 0);

            } finally {
                response = jsonBuilder.build().toString();
            }
        }
        return response;

    }
}
