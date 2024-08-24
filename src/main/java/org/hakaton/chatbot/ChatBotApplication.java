package org.hakaton.chatbot;


import org.hakaton.chatbot.auth.Authorization;
import org.hakaton.chatbot.bot.MyBot;
import org.hakaton.chatbot.chatai.ChatAi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Scanner;

//@SpringBootApplication
public class ChatBotApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ChatBotApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            try {
                botsApi.registerBot(new MyBot());
//                ChatAi chatAi = new ChatAi();
//                Scanner scanner = new Scanner(System.in);
//                while (true) {
//                    String message = scanner.nextLine();
//                    String answer = chatAi.sentMessage(message);
//                    System.out.println(answer);
//                }
//            } catch (IOException | InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
