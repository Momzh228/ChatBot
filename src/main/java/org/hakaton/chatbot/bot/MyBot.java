package org.hakaton.chatbot.bot;

import org.hakaton.chatbot.chatai.ChatAi;
import org.hakaton.chatbot.search.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
public class MyBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken = "7464425246:AAFaqP5sSOmrPDC0JJYfQ2DwpgB10fm2w2Y";

    @Value("${telegram.bot.username}")
    private String botUsername = "Information_Search_Kogeki_Bot";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            try {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                String context = new SearchService().search(messageText);
                System.out.println(context);
                message.setText(context);
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
