//package org.hakaton.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RestController
//@RequestMapping("/api")
//public class ApiController {

//    @Autowired
//    private RestTemplate restTemplate;

//    @GetMapping("/send-oauth-request")
//    public ResponseEntity<String> sendOAuthRequest() throws IOException {
//        System.out.println("gfdgfd");
//        String url = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth"; // Замените на нужный URL
//        String rquid = "24ecd146-b082-4752-92ed-54528d828624"; // Замените на свой идентификатор запроса
//        String authorization = "ZmJmNDE1Y2YtYWZjZS00NDVjLTllZTMtOTgyYzBjZTNjYTg0OjI0ZWNkMTQ2LWIwODItNDc1Mi05MmVkLTU0NTI4ZDgyODYyNA=="; // Замените на свои авторизационные данные
//        String scope = "GIGACHAT_API_PERS"; // Замените на нужную область
//       HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(rquid)).rquid
//
//    }
//}