package com.textSummarization;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class Util {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try{
/*        HttpResponse request = Unirest.post("https://textanalysis-text-summarization.p.mashape.com/text-summarizer")
                .header("X-Mashape-Authorization", "Your Mashape API Key")
                .header("Content-Type", "application/json")
                .body("{\"url\":\"http://en.wikipedia.org/wiki/Automatic_summarization\",\"text\":\"\",\"sentnum\":8}")
                //.body("{\"url\":\"http:\/\/en.wikipedia.org\/wiki\/Automatic_summarization\",\"text\":\"\",\"sentnum\":8}")
                .asJson();*/
        
        
     // These code snippets use an open-source library. http://unirest.io/java
        HttpResponse<JsonNode> response = Unirest.post("https://textanalysis-text-summarization.p.mashape.com/text-summarizer")
        .header("X-Mashape-Key", "6TZzfGPoSXmsh7gvPLbMlMQ8UsiHp1EJAXxjsnDIR1diOa9prm")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body("{\"url\":\"http://en.wikipedia.org/wiki/Automatic_summarization\",\"text\":\"\",\"sentnum\":8}")
        .asJson();
        System.out.println(response.getBody());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
