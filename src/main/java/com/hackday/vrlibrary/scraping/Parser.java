package com.hackday.vrlibrary.scraping;

import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by vesel on 2016-03-25.
 */

public class Parser {
    private JSONObject json;
    private List<WikiPage> pages;
    public Parser(URL url, int depth) {
        // fetches the initial resource (by url)
    }


    public JSONObject toJSON() {
        return json;
    }


}
