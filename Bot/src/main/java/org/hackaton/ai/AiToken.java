package org.hackaton.ai;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class AiToken {
    private final String RQ_ID = "24ecd146-b082-4752-92ed-54528d828624";
    private final String AUTH_HEADER = "Basic ZmJmNDE1Y2YtYWZjZS00NDVjLTllZTMtOTgyYzBjZTNjYTg0OjI0ZWNkMTQ2LWIwODItNDc1Mi05MmVkLTU0NTI4ZDgyODYyNA==";

    public String createToken()  {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
        String formData = "scope=GIGACHAT_API_PERS";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("RqUID", RQ_ID)
                .header("Authorization", AUTH_HEADER)
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
        JSONObject json = new JSONObject(response.body());
        return json.getString("access_token");
    }

}
