package org.hackaton.answer;

import org.hackaton.ai.AiAnswer;
import org.hackaton.search.HtmlParser;
import org.hackaton.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private  final SearchService  searchService;
    private final HtmlParser  htmlParser;
    private  final AiAnswer aiAnswer;

   @Autowired
    public AnswerService(SearchService searchService, HtmlParser htmlParser, AiAnswer aiAnswer) {
        this.searchService = searchService;
        this.htmlParser = htmlParser;
        this.aiAnswer = aiAnswer;
    }
    public String getAnswer(String query){
       return null;
    }
}
