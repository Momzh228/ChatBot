package org.hackaton.search;

import org.hackaton.ai.AiAnswer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class HtmlParser {

    public String parseHtml(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element content = doc.select("#mw-content-text").first();
            String text = content != null ? content.text() : "Блок не найден.";
            return text.length() > 1000 ? text.substring(0, 1000) : text;
        } catch (IOException e) {
            return "Ошибка при загрузке страницы.";
        }
    }
}