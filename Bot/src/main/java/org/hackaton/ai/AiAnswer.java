package org.hackaton.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Component
public class AiAnswer {
    private final AiToken aiToken;
    private String token;

    @Autowired
    public AiAnswer(AiToken aiToken) {
        this.aiToken = aiToken;
        initializeToken();
    }
     private void initializeToken(){
         token = aiToken.createToken();
     }

    public  String extractMessageContent(String jsonResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode choicesArray = rootNode.get("choices");
        JsonNode firstChoice = choicesArray.get(0);
        JsonNode messageNode = firstChoice.get("message");
        return messageNode.get("content").asText();
    }

    public String sentMessage(String message) throws IOException, InterruptedException {
        System.out.println(message);
//        message = "морковь ответь коротко";
        String url = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";
        String requestBody = "{\n" +
                "  \"model\": \"GigaChat\",\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"" + message + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"stream\": false,\n" +
                "  \"repetition_penalty\": 1\n" +
                "}";
        System.out.println(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() == 200) {
            return extractMessageContent(response.body());
        } else if (response.statusCode() == 401) {
            initializeToken();
            return null;
        } else {
            throw new RuntimeException("Failed : HTTP error code1 : " + response.statusCode());
        }
    }
}
