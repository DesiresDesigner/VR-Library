package com.hackday.vrlibrary.scraping;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vesel on 2016-03-25.
 */

public class Parser {

    private JSONObject json;

    /**
     * fetches the pages from the www
     * @param url
     * @param depth
     */
    public Parser(URL url, int depth) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }

    public JSONObject toJSON() {
        return json;
    }

}
