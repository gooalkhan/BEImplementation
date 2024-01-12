package com.example.beimplementation.Services;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class HttpRequestService {

    JSONObject getFromAPI(String fullEndPoint) throws Exception {
        URL url = new URL(fullEndPoint);

        log.debug("trying to connect - {}", fullEndPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try (AutoCloseable a = conn::disconnect) {

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);

            if (conn.getResponseCode() != 200) {
                log.error("HttpRequest job failed, status code: {}", conn.getResponseCode());
                throw new HttpClientErrorException(HttpStatus.valueOf(conn.getResponseCode()));

            } else {
                log.debug("HttpRequest connection successful, status code: {}", conn.getResponseCode());
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();

                while (br.ready()) {
                    sb.append(br.readLine());
                }

                String responseString = sb.toString();
                log.debug(responseString);

                return new JSONObject(responseString).getJSONObject("response");
            }
        }
    }
}
