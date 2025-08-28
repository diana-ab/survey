package org.example.ai;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;


public class AiClient {
    private OkHttpClient client;
    private String baseUrl;
    private String id;

    public AiClient(String id) {
        this.client = new OkHttpClient();
        this.baseUrl = "https://app.seker.live/fm1/send-message";
        this.id = id;
    }

    public String messageGpt(String message) {
        try {
            String url = this.baseUrl + "?id=" + id +
                    "&text=" + message;
            Request request = new Request.Builder().url(url).build();
            Response response = this.client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString("extra");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
