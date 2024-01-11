package com.example.beimplementation.Controllers;

import com.example.beimplementation.Entities.MidLandFcst;
import com.example.beimplementation.Exceptions.BadQueryException;
import com.example.beimplementation.Exceptions.MultipleEntryException;
import com.example.beimplementation.Services.MidFcstInfoService;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/midfcst")
@RestController
public class MidFcstInfoController {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    private final MidFcstInfoService midFcstInfoService;

    @GetMapping(value="/getMidLandFcst", produces ="application/json")
    public String getMidLandFcst(
            @RequestParam(value = "numOfRows", defaultValue = "1") String numOfRows,
            @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
            @RequestParam(value = "regId") String regId,
            @RequestParam(value = "tmFc", defaultValue = "") String tmFc
    ) {
        ResponseJsonBuilder jsonBuilder = new ResponseJsonBuilder();
        String response;

        if (tmFc.isEmpty()) tmFc = midFcstInfoService.getLatestUpdateTime();

        try {
            MidLandFcst midLandFcst = midFcstInfoService.getMidLandFcst(apikey, Integer.parseInt(numOfRows), Integer.parseInt(pageNo), regId, tmFc);
            jsonBuilder.setHeader("00", "NORMAL_SERVICE");
            jsonBuilder.setBody(1, 1);
            jsonBuilder.appendItem(new JSONObject(midLandFcst));

        } catch (BadQueryException bqe) {
            log.error("bad query");
            jsonBuilder.setHeader(bqe.getResultCode(), bqe.getResultMsg());
            jsonBuilder.setBody(0, 0);

        } catch (MultipleEntryException me) {
            log.error("found multiple entries");
            jsonBuilder.setHeader("98", "SERVER_ERROR");
            jsonBuilder.setBody(0, 0);

        } catch (HttpClientErrorException hee) {
            log.error("cannot connect to origin - {}", hee.getStatusCode());
            jsonBuilder.setHeader("99", "CONNECTION_ERROR");
            jsonBuilder.setBody(0, 0);
        } catch (Exception e) {
            log.error(e.getMessage());
            jsonBuilder.setHeader("98", "SERVER_ERROR");
            jsonBuilder.setBody(0, 0);
        } finally {
            response = jsonBuilder.build().toString();
        }
        return response;
    }
}
