package com.hackday.vrlibrary.scraping;

import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by vesel on 2016-03-26.
 */
public class WikiPage {
    public URL url;
    public String title;
    public String content;
    public List<WikiPage> connections;

    public WikiPage(URL url, String title) {
        this.url = url;
        this.title = title;
    }

    public WikiPage(URL url, int depth) {
        try {
            WikiPage initial = new WikiPage(url, "dummy title, change later");
            initial.content = fetchResource(url);
            // finds connections
            if (depth > 1) {
                findConnections(initial, depth - 1);
            }
            // fetches the neighbours & parses them as well
        } catch (IOException ex) {
            System.out.println("fail");
        }
    }

    private String fetchResource(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine, str = "";
        while ((inputLine = in.readLine()) != null) {
            str += inputLine;
        }
        in.close();
        return str;
        // TODO: check if functions properly
        // TODO: rewrite to streams
    }

    private void findConnections(WikiPage page, int depth) {
        // TODO
        List<URL> urls = parseURLs(page);
        for (URL url : urls) {
            page.connections.add(new WikiPage(url, depth));
        }
    }

    private List<URL> parseURLs(WikiPage page) {
        throw new NotImplementedException();
    }

    public JSONObject toJSONObject(String id) {
        JSONObject obj = new JSONObject();
        obj.append("id", id);
        obj.append("url", url);
        obj.append("title", title);
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WikiPage wikiPage = (WikiPage) o;

        return url != null ? url.equals(wikiPage.url) : wikiPage.url == null;

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
