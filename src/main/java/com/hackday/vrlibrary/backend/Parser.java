package com.hackday.vrlibrary.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DesiresDesigner on 3/26/16.
 */
public class Parser {
    private String name;
    Element body;
    List<Elements> blocks;
    Map<String, List<String>> linkMap;

    Parser(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        body = doc.body();
        blocks = new ArrayList<>();
        linkMap = new HashMap<>();

        name = doc.select("title").html();
        name = name.replace(" — Википедия", "");
    }

    private void generateMainInfoBlock() {
        Element sibling = body.select("#mw-content-text").select("p").get(0);
        blocks.add(new Elements());
        while (sibling != null) {
            if (sibling.tagName() == "h2" || sibling.id().equals("toc")) {
                break;
            }
            blocks.get(0).add(sibling);
            sibling = sibling.nextElementSibling();
        }
    }

    private void generateContentBlocks() {
        int index = 0;
        Element sibling = body.select("#mw-content-text").select("h2").get(1);
        while (sibling != null) {
            if (sibling.tagName() == "h2") {
                ++index;
                blocks.add(new Elements());
            }
            blocks.get(index).add(sibling);
            sibling = sibling.nextElementSibling();
        }
    }

    private void parseBlocks() {
        Pattern sentenceRegex = Pattern.compile("(.+?\\. )|(.+(\\.\\.))"),
                linkRegex = Pattern.compile("href=\"(.*?)\"");

        for (Elements block : blocks) {
            String topic = block.select("h2").html();
            topic = topic.replaceAll("<[^>]*>", "");
            topic = topic.replaceAll("\\[править \\| править вики-текст\\]", "");
            for (Element child : block) {
                String text = child.html();
                text = text.replaceAll("(<\\/li>)|(<\\/div>)|(<\\/p>)|(<br>)|(<br\\/>|(<br \\/>))", ".");
                text = text.replaceAll("<(?!\\/?a(?=>|\\s.*>))\\/?.*?>", " ");
                Matcher mSentence = sentenceRegex.matcher(text);
                while (mSentence.find()) {
                    Matcher mLink = linkRegex.matcher(mSentence.group());
                    String sentence = mSentence.group().replaceAll("<[^>]*>", "");
                    while (mLink.find()) {
                        String link = mLink.group();
                        link = link.replaceAll("(href=)|(\")", "");
                        if (!linkMap.containsKey(link))
                            linkMap.put(link, new ArrayList<>());
                        linkMap.get(link).add(topic + ": " + sentence);
                    }
                }
            }
        }
    }

    public void parse() {
        blocks.clear();
        linkMap.clear();
        generateMainInfoBlock();
        generateContentBlocks();
        parseBlocks();
    }

    public String getName() {
        return name;
    }

    public Map<String, List<String>> getLinkMap() {
        return linkMap;
    }

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("https://ru.wikipedia.org/wiki/%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_%28%D1%86%D0%B8%D0%BA%D0%BB%29");
        parser.parse();
        System.out.println(parser.getName());
        Map<String, List<String>> linkMap = parser.getLinkMap();
        for (String key : linkMap.keySet()) {
            System.out.println(key + "   :");
            for (String sentence : linkMap.get(key)) {
                System.out.println("----------- " + sentence);
            }
        }

        /*Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_%28%D1%86%D0%B8%D0%BA%D0%BB%29").get();
        Element body = doc.body();

        List<Elements> blocks = new ArrayList<>();
        int index = 0;

        //Element sibling = body.select("#mw-content-text").select("h2").get(1);
        Element sibling = body.select("#mw-content-text").select("p").get(0);
        blocks.add(new Elements());
        while (sibling != null) {
            if (sibling.tagName() == "h2" || sibling.id().equals("toc")) {
                break;
            }
            blocks.get(index).add(sibling);
            sibling = sibling.nextElementSibling();
        }
        sibling = body.select("#mw-content-text").select("h2").get(1);
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
        }*/
    }
}