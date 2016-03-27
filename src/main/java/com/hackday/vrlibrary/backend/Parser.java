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
    private Document doc;
    private String name;
    private Element body;
    private String mainInfo;
    private List<Elements> blocks;
    private Map<String, List<String>> linkMap;

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
        mainInfo = "";
        while (sibling != null) {
            if (sibling.tagName() == "h2" || sibling.id().equals("toc")) {
                break;
            }
            blocks.get(0).add(sibling);
            mainInfo += sibling.html();
            sibling = sibling.nextElementSibling();
        }
        mainInfo = mainInfo.replaceAll("<[^>]*>", "");
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
        Pattern sentenceRegex = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)"), //(.+?\. )|(.+(\.\.))
                linkRegex = Pattern.compile("href=\"(.*?)\"");

        for (Elements block : blocks) {
            String topic = block.select("h2").html();
            topic = topic.replaceAll("<[^>]*>", "");
            topic = topic.replaceAll("\\[править \\| править вики-текст\\]", "");
            for (Element child : block) {
                String text = child.html();
                text = text.replaceAll("(<\\/li>)|(<\\/div>)|(<\\/p>)|(<br>)|(<br ?\\/>)|(;)", ".");
                text = text.replaceAll("<(?!\\/?a(?=>|\\s.*>))\\/?.*?>", " ");
                text = text.replaceAll("(<\\w+)[^>]*( href=[^ ]*)[^>]*(>)", "$1$2$3");
                text = text.replaceAll("([A-ZА-Я][a-zа-я]{1,2})\\. ", "$1.");
                Matcher mSentence = sentenceRegex.matcher(text);
                while (mSentence.find()) {
                    Matcher mLink = linkRegex.matcher(mSentence.group());
                    String sentence = mSentence.group().replaceAll("<[^>]*>", "").replaceAll("\\\n", "");
                    while (mLink.find()) {
                        String link = mLink.group();
                        link = link.replaceAll("(href=)|(\")", "");
                        if (!link.substring(0, 5).equals("/wiki") || sentence == "")
                            continue;
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

    public String getMainInfo() {
        return mainInfo;
    }

    public void setUrl(String url) throws IOException {
        doc = Jsoup.connect(url).get();
        body = doc.body();
        blocks.clear();
        linkMap.clear();
        name = doc.select("title").html();
        name = name.replace(" — Википедия", "");
    }

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("https://ru.wikipedia.org/wiki/%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_%28%D1%86%D0%B8%D0%BA%D0%BB%29");
        parser.parse();
        System.out.println(parser.getName());

        Map<String, List<String>> linkMap = parser.getLinkMap();
        System.out.println(parser.getMainInfo());

        for (String key : linkMap.keySet()) {
            System.out.println(key + "   :");
            for (String sentence : linkMap.get(key)) {
                System.out.println("----------- " + sentence);
            }
        }

        /*String text = "<li id=\"ca-view\" class=\"collapsible\"><span><a href=\"/w/index.php?title=%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_(%D1%86%D0%B8%D0%BA%D0%BB)&amp;stable=1\" >Читать</a></span></li>\n" +
                "<li id=\"ca-current\" class=\"collapsible collapsible selected\"><span><a href=\"/w/index.php?title=%D0%A2%D1%91%D0%BC%D0%BD%D0%B0%D1%8F_%D0%91%D0%B0%D1%88%D0%BD%D1%8F_(%D1%86%D0%B8%D0%BA%D0%BB)&amp;stable=0&amp;redirect=no\"  title=\"Показать текущую версию этой страницы [v]\" accesskey=\"v\">Текущая версия</a></span></li>\n" +
                "<form action=\"/w/index.php\" id=\"searchform\">\n" +
                "<div id=\"simpleSearch\">\n" +
                "<input type=\"search\" name=\"search\" placeholder=\"Поиск\" title=\"Искать в Википедии [f]\" accesskey=\"f\" id=\"searchInput\" /><input type=\"hidden\" value=\"Служебная:Поиск\" name=\"title\" /><input type=\"submit\" name=\"fulltext\" value=\"Найти\" title=\"Найти страницы, содержащие указанный текст\" id=\"mw-searchButton\" class=\"searchButton mw-fallbackSearchButton\" /><input type=\"submit\" name=\"go\" value=\"Перейти\" title=\"Перейти к странице, имеющей в точности такое название\" id=\"searchButton\" class=\"searchButton\" />\t\t\t\t\t\t\t</div>\n" +
                "</form>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div id=\"mw-panel\">\n" +
                "<div id=\"p-logo\" role=\"banner\"><a class=\"mw-wiki-logo\" href=\"/wiki/%D0%97%D0%B0%D0%B3%D0%BB%D0%B0%D0%B2%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0\"  title=\"Перейти на заглавную страницу\"></a></div>\n" +
                "<div class=\"portal\" role=\"navigation\" id='p-navigation' aria-labelledby='p-navigation-label'>" +
                "<p>В 2007 году компания <a href=\"/wiki/IGN\" title=\"IGN\">IGN Movies</a> сообщила, что ведет работу над <a href=\"/wiki/%D0%A4%D0%B8%D0%BB%D1%8C%D0%BC\" title=\"Фильм\">фильмом</a> о приключениях Роланда. Сообщалось, что его <a href=\"/wiki/%D0%9F%D1%80%D0%BE%D0%B4%D1%8E%D1%81%D0%B5%D1%80\" title=\"Продюсер\">продюсером</a> и <a href=\"/wiki/%D0%A0%D0%B5%D0%B6%D0%B8%D1%81%D1%81%D1%91%D1%80\" title=\"Режиссёр\">режиссёром</a> станет <a href=\"/wiki/%D0%90%D0%B1%D1%80%D0%B0%D0%BC%D1%81,_%D0%94%D0%B6%D0%B5%D1%84%D1%84%D1%80%D0%B8_%D0%94%D0%B6%D0%B5%D0%B9%D0%BA%D0%BE%D0%B1\" title=\"Абрамс, Джеффри Джейкоб\">Дж. Дж. Абрамс</a>.<sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[2]</a></sup> <i>Дж. Дж. Абрамс</i> и <i><a href=\"/wiki/%D0%9B%D0%B8%D0%BD%D0%B4%D0%B5%D0%BB%D0%BE%D1%84,_%D0%94%D0%B5%D0%B9%D0%BC%D0%BE%D0%BD_%D0%9B%D0%BE%D1%80%D0%B5%D0%BD%D1%81\" title=\"Линделоф, Деймон Лоренс\" class=\"mw-redirect\">Деймон Линделоф</a></i> купили права на экранизацию серии «Темная Башня» за 19 <a href=\"/wiki/%D0%94%D0%BE%D0%BB%D0%BB%D0%B0%D1%80_%D0%A1%D0%A8%D0%90\" title=\"Доллар США\">долларов</a> (это <a href=\"/wiki/%D0%A7%D0%B8%D1%81%D0%BB%D0%BE\" title=\"Число\">число</a> является одним из ключевых понятий в книгах цикла).<sup id=\"cite_ref-3\" class=\"reference\"><a href=\"#cite_note-3\">[3]</a></sup> Согласно выпуску №&#160;923 <i>«Entertainment Weekly»</i>, Кинг «является приверженцем пустынно-островных пейзажей и доверяет Абрамсу воплотить на пленке свои фантазии», и Линделоф, скорее всего, будет «наиболее вероятным кандидатом в <a href=\"/wiki/%D0%A1%D1%86%D0%B5%D0%BD%D0%B0%D1%80%D0%B8%D1%81%D1%82\" title=\"Сценарист\">сценаристы</a> для первой части».<sup id=\"cite_ref-4\" class=\"reference\"><a href=\"#cite_note-4\">[4]</a></sup></p>\n" +
                "<p>На видеоресурсе <a href=\"/wiki/Youtube.com\" title=\"Youtube.com\" class=\"mw-redirect\">Youtube.com</a> размещено огромное количество трейлеров, имитирующих фильмы о Темной Башне. Официальный Гран-При издательского агентства <i>«Simon &amp; Schuster’s»</i> в конкурсе <i>«Американский стрелок»</i> получил ролик <i>«Встреча Роланда и Брауна»</i><sup id=\"cite_ref-5\" class=\"reference\"><a href=\"#cite_note-5\">[5]</a></sup>, снятый Робертом Дэвидом Кокрейном (<a href=\"/wiki/%D0%90%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9_%D1%8F%D0%B7%D1%8B%D0%BA\" title=\"Английский язык\">англ.</a>&#160;<i><span lang=\"en\" xml:lang=\"en\">Robert David Cochrane</span></i>).<sup id=\"cite_ref-6\" class=\"reference\"><a href=\"#cite_note-6\">[6]</a></sup></p>\n" +
                "<p>В снятом в <a href=\"/wiki/2007_%D0%B3%D0%BE%D0%B4\" title=\"2007 год\">2007&#160;году</a> фильме <a href=\"/wiki/%D0%9C%D0%B3%D0%BB%D0%B0_(%D1%84%D0%B8%D0%BB%D1%8C%D0%BC)\" title=\"Мгла (фильм)\">«Мгла»</a> главный герой, <a href=\"/wiki/%D0%A5%D1%83%D0%B4%D0%BE%D0%B6%D0%BD%D0%B8%D0%BA\" title=\"Художник\">художник</a> Дэвид Драйтон показан рисующим <a href=\"/wiki/%D0%9F%D0%BB%D0%B0%D0%BA%D0%B0%D1%82\" title=\"Плакат\">постер</a> к фильму с Роландом в центре, стоящим перед дверью из железного дерева, с розой и Темной Башней по обеим её сторонам.</p>\n" +
                "<p>В феврале 2008 Абрамс заявил, что он и Линделоф приступили к написанию чернового варианта сценария<sup id=\"cite_ref-7\" class=\"reference\"><a href=\"#cite_note-7\">[7]</a></sup>. Но в ноябре 2009 Абрамс объявил: «Вряд ли найдётся больший фанат „Тёмной Башни“, чем я, но это наверное хорошая причина тому, чтобы адаптировал этот материал не я. После шести лет работы над сериалом „Остаться в живых“, последнее, чем я хочу быть занят&#160;— это потратить ещё семь лет на адаптацию самого моего любимого цикла книг. Я огромный фанат Стивена Кинга, так что я ужасно боюсь это испортить. Я бы всё отдал, чтобы кто-то другой написал сценарий. Я думаю, что эти фильмы сняты будут, потому что они настолько великолепны. Но только не мной».<sup id=\"cite_ref-8\" class=\"reference\"><a href=\"#cite_note-8\">[8]</a></sup></p>\n" +
                "<p>8 ";

        //String text = "Kakfkd kjdfdvv JKNkj Kjfnj. Kakfkd kjdfdvv JKNkj Kj. Kjhgh. KJHjhbh jhjh jggyvb.";
        text = text.replaceAll("(<\\/li>)|(<\\/div>)|(<\\/p>)|(<br>)|(<br\\/>|(<br \\/>))", ".");
        text = text.replaceAll("<(?!\\/?a(?=>|\\s.*>))\\/?.*?>", " ");

        text = text.replaceAll("(<\\w+)[^>]*( href=[^ ]*)[^>]*(>)", "$1$2$3");
        text = text.replaceAll("([A-ZА-Я][a-zа-я]{1,2})\\. ", "$1.");

        //System.out.println(text);
        Pattern sentence1Regex = Pattern.compile("(.+?\\. )|(.+(\\.\\.))"),
                sentenceRegex = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)"); //([A-ZА-Я][a-zа-я]{1,2})

        Matcher mSentence = sentenceRegex.matcher(text);
        while (mSentence.find()) {
            System.out.println(mSentence.group());
            System.out.println();
        }*/
    }
}