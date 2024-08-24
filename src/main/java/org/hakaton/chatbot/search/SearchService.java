package org.hakaton.chatbot.search;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class SearchService {
//    @Value("${google.apiKey}")
    private String apiKey = "AIzaSyDCpwRkaV0zOjxUEjAtWDweVwZwrhOX-hE";

//    @Value("${google.searchEngineId}")
    private String searchEngineId = "b219cd05d084b4c38";
    private final Integer num = 1;
    private  String query;

    public String search(String query) {
        this.query = query;
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
            String context = new WikiParser().parseHtml(link, query);
            results.append(context).append("\n");
            results.append("Link: ").append(link).append("\n");
        }
        return results.toString();
    }
}

