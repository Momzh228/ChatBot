package org.hackaton.search;

import org.hackaton.ai.AiAnswer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class SearchService {
    @Value("${google.apiKey}")
    private String apiKey;

    @Value("${google.searchEngineId}")
    private String searchEngineId;
    private final Integer num = 1;
    private final AiAnswer aiAnswer;
    @Autowired

    public SearchService(AiAnswer aiAnswer) {
        this.aiAnswer = aiAnswer;
    }

    public String search(String query) {
        String encodedQuery = encodeQuery(query);
        String urlString = buildUrl(encodedQuery);

        try {
            String response = getHttpResponse(urlString);
            return parseResponse(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "0";
    }

    private String encodeQuery(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            System.out.println("Ошибка кодирования запроса:" + e.getMessage());
            return null;
        }
    }

    private String buildUrl(String encodedQuery) {
        try {
            return String.format("https://www.googleapis.com/customsearch/v1?q=%s&cx=%s&key=%s&num=%d&siteSearch=%s",
                    encodedQuery, searchEngineId, apiKey, num, URLEncoder.encode("wikipedia.org", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding error: " + e.getMessage());
            return "";
        }
    }




    private String getHttpResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private String parseResponse(String response) {
        StringBuilder results = new StringBuilder();
        JSONObject json = new JSONObject(response);
        JSONArray items = json.optJSONArray("items");

        if (items != null && !items.isEmpty()) {
            JSONObject item = items.getJSONObject(0);
            String link = item.optString("link", "Нет ссылки");
            String context = new HtmlParser().parseHtml(link);
            String answer;
            try {
                 answer = aiAnswer.sentMessage(context);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
            results.append(answer).append("\n");
            results.append("Link: ").append(link).append("\n");
        }
        return results.toString();
    }
}
