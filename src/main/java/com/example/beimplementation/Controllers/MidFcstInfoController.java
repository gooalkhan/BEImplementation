package com.example.beimplementation.Controllers;

import com.example.beimplementation.Entities.MidFcst;
import com.example.beimplementation.Services.MidFcstInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/midfcst")
@RestController
public class MidFcstInfoController {

    @Value("${openapi.midfcst.key}")
    private String apikey;

    private final MidFcstInfoService midFcstInfoService;

    @GetMapping(value = "/getMidFcst", produces = "application/json")
    public MidFcst getMidFcst(
            @RequestParam(value = "numOfRows", defaultValue = "1") String numOfRows,
            @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
            @RequestParam(value = "dataType", defaultValue = "JSON") String dataType,
            @RequestParam(value = "stnId") String stnId,
            @RequestParam(value = "tmFc", defaultValue = "") String tmFc
    ) {
        MidFcst midFcst = null;

        if (tmFc.isEmpty()) tmFc = midFcstInfoService.getLatestUpdateTime();

        try {
            midFcst = midFcstInfoService.getMidFcst(apikey, Integer.parseInt(numOfRows), Integer.parseInt(pageNo), dataType, Integer.parseInt(stnId), tmFc);

        } catch (Exception e) {
            log.error("MidFcst request failed");
        }
        return midFcst;
    }
}
