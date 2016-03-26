package com.hackday.vrlibrary.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DesiresDesigner on 3/26/16.
 */
public class Parser {

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_%28%D1%86%D0%B8%D0%BA%D0%BB%29").get();
        Element body = doc.body();
        //Elements links = body.select("a[href]");
        //Elements topics = body.getElementsByClass("mw-headline");

        List<Elements> blocks = new ArrayList<>();
        int index = -1;

        Element sibling = body.select("#mw-content-text").select("h2").get(1);
        while (sibling != null) {
            if (sibling.tagName() == "h2") {
                ++index;
                blocks.add(new Elements());
            }
            blocks.get(index).add(sibling);
            sibling = sibling.nextElementSibling();
        }

        Pattern sentenceRegex = Pattern.compile("(.+?\\. )|(.+(\\.\\.))"),
                linkRegex = Pattern.compile("href=\"(.*?)\"");

        Map<String, String> keyMap = new HashMap<>();
        for (Elements block : blocks) {
            for (Element child : block) {
                String text = child.html();
                text = text.replaceAll("(<\\/li>)|(<\\/div>)|(<\\/p>)|(<br>)|(<br\\/>|(<br \\/>))", ".");
                text = text.replaceAll("<(?!\\/?a(?=>|\\s.*>))\\/?.*?>", " ");
                Matcher mSentence = sentenceRegex.matcher(text);
                while (mSentence.find()) {
                    Matcher mLink = linkRegex.matcher(mSentence.group());
                    String sentence = mSentence.group().replaceAll("<[^>]*>", "");
                    //sentence = sentence.substring(6, sentence.length());
                    while (mLink.find()) {
                        String link = mLink.group();
                        link = link.replaceAll("(href=)|(\")", "");
                        System.out.println(link + " - " + sentence);
                    }
                    //System.out.println(mSentence.group());
                    System.out.println();
                }
            }
        }
    }
}

/*
                Matcher linkMatcher = linkRegex.matcher(child.html());
                while (linkMatcher.find()) {
                    String link = linkMatcher.group(2);
                    link = link.substring(0, link.length() - 1);

                    if(link.charAt(0) == '/') {
                        link = "https://ru.wikipedia.org" + link;
                    } else if(link.charAt(0) == '#') {
                        link = null;
                    }

                    if(link != null) {

                        Matcher nameMatcher = nameRegex.matcher(child.html());
                        if (nameMatcher.find()) {
                            String name = nameMatcher.group(2);
                            keyMap.put(name, link);

                        } else {
                            keyMap.put("None" + noneId, link);
                            noneId += 1;
                        }
                    }
                }
 */