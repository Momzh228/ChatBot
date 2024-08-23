package org.hackaton.search;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Component
public class SearchService {
    String apiKey = "AIzaSyDCpwRkaV0zOjxUEjAtWDweVwZwrhOX-hE";
    String searchEngineId = "b219cd05d084b4c38";
    public void search(String query){
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlString = "https://www.googleapis.com/customsearch/v1?q=" + encodedQuery + "&cx=" + searchEngineId + "&key=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Парсинг JSON ответа
            JSONObject json = new JSONObject(response.toString());
            JSONArray items = json.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String title = item.getString("title");
                String link = item.getString("link");
                String snippet = item.getString("snippet");

                System.out.println("Title: " + title);
                System.out.println("Link: " + link);
                System.out.println("Snippet: " + snippet);
                System.out.println("===================================");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
}
