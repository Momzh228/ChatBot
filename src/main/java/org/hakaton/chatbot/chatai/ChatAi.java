package org.hakaton.chatbot.chatai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hakaton.chatbot.auth.Authorization;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ChatAi {

    private final Authorization auth = new Authorization();
    private String token;

    public ChatAi() throws IOException, InterruptedException {
        token = auth.createToken();
    }

    public static String extractMessageContent(String jsonResponse) throws JsonProcessingException {
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
            return token = auth.createToken();
        } else {
            throw new RuntimeException("Failed : HTTP error code1 : " + response.statusCode());
        }
    }

}
