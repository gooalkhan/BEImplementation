package com.example.beimplementation.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

//콘트롤러단에서 json리턴시 json에 메타데이터 추가하는 빌더패턴
public class ResponseJsonBuilder {
    //결과 코드 및 메세지 추가부분
    private JSONObject header;
    //자료 개수나 페이징 정보가 담김
    private JSONObject body;

    //리턴하는 자료가 속한 부분
    private JSONArray itemArray = new JSONArray();

    //잘못된 빌드시에도 예외를 발생시키지 않도록 생성시에 잘못된 빌드라고 메시지 생성
    public ResponseJsonBuilder() {
        //에러코드는 기상청 에러코드와 겹치지 않기위해 90번대를 사용(기상청 에러코드 문서화가 되어있지않음)
        this.setHeader("96", "FAILED_MAKING_RESPONSE");
        this.setBody(0,0);
    }

    public void setHeader(String resultCode, String resultMsg) {
        header = new JSONObject();

        header.put("resultCode", resultCode);
        header.put("resultMsg", resultMsg);
    }

    public void setBody(int numOfRows, int totalCount) {
        body = new JSONObject();

        body.put("dataType", "JSON");
        body.put("numOfRows", numOfRows);
        body.put("totalCount", totalCount);
    }

    public void appendItem(JSONObject jsonObject) {
        itemArray.put(jsonObject);
    }

    //작성 완료시 빌드를 호출하여 JSON객체 생성
    public JSONObject build() {
        JSONObject jsonObject = new JSONObject();
        JSONObject response = new JSONObject();

        JSONObject item = new JSONObject();
        item.put("item", this.itemArray);
        this.body.put("items", item);

        response.put("header", header);
        response.put("body", body);

        jsonObject.put("response", response);

        return jsonObject;
    }
}
