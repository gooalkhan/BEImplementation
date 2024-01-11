package com.example.beimplementation.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResponseJsonBuilder {
    private JSONObject header;
    private JSONObject body;

    private JSONArray itemArray = new JSONArray();

    public ResponseJsonBuilder() {
        this.setHeader("05", "FAILED_MAKING_RESPONSE");
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
