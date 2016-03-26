package com.hackday.vrlibrary.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by DesiresDesigner on 3/26/16.
 */
public class Parser {

    static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_%28%D1%86%D0%B8%D0%BA%D0%BB%29").get();
        Elements links = doc.select("a[href]");
//        Elements topics = doc.getElementsByClass("mw-headline");
    }
}
