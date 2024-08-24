package org.hakaton.chatbot.search;

import org.hakaton.chatbot.chatai.ChatAi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WikiParser {

    public String parseHtml(String url, String query) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element content = doc.select("#mw-content-text").first();
            String text = content != null ? content.text() : "Блок не найден.";
            ChatAi chatAi = new ChatAi();
            text = text.length() > 1000 ? text.substring(0, 1000) : text;
            text = chatAi.sentMessage("выведи самый подходящий текст по вопросу " + query + " из блока информации:" + text);
            System.out.println(text);
            return text;
        } catch (IOException e) {
            return "Ошибка при загрузке страницы.";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}