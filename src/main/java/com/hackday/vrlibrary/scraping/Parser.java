package com.hackday.vrlibrary.scraping;

import org.json.JSONArray;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vesel on 2016-03-25.
 */

public class Parser {
    private JSONObject json;
    private Set<WikiPage> pages;
    private Map<String, WikiPage> p;
    public Parser(URL url, int depth) {
        // fetches the initial resource (by url)
        WikiPage initial = new WikiPage(url, 2);
        json = new JSONObject();
        JSONArray nodes = new JSONArray(),
                edges = new JSONArray();
        pages.add(initial);
        nodes.put(initial.toJSONObject(initial.title));

        for (WikiPage firstLevelNode : initial.connections) {
            JSONObject o = firstLevelNode.toJSONObject(firstLevelNode.title);
            JSONObject edge = new JSONObject();
            edge.append("source", initial.title);
            edge.append("target", firstLevelNode.title);
            edges.put(edge);
            for (WikiPage secondLevelNode : firstLevelNode.connections) {
                nodes.put(secondLevelNode.toJSONObject(firstLevelNode.title));
                JSONObject edge1 = new JSONObject();
                edge.append("source", firstLevelNode.title);
                edge.append("target", secondLevelNode.title);
                edges.put(edge1);
            }
        }
        json.append("nodes", nodes);
        json.append("edges", edges);
    }


    public JSONObject toJSON() {
        return json;
    }


}
