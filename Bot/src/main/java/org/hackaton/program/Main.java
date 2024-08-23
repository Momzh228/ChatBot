package org.hackaton.program;

import org.hackaton.bot.SearchBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        String nameBot = "Information_Search_Kogeki_Bot";
        String tokenBot = "7464425246:AAFaqP5sSOmrPDC0JJYfQ2DwpgB10fm2w2Y";
        try {
            new SearchBot(nameBot, tokenBot);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
