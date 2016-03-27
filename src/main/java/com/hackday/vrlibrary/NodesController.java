package com.hackday.vrlibrary;

import com.hackday.vrlibrary.backend.Parser;
import com.hackday.vrlibrary.backend.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by maxim on 26.03.2016.
 */
@RestController
public class NodesController {
    @RequestMapping(value = "/graph", method = RequestMethod.GET)
    public ResponseEntity<String> getGraph(@RequestParam(value = "title") String name) {
        String json  = "{\n" +
                "  \"nodes\": [\n" +
                "    {\n" +
                "      \"id\": \"0\",\n" +
                "      \"title\": \"Cardboard\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"1\",\n" +
                "      \"title\": \"Autobus\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"2\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"3\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"4\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"5\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"6\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"7\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }, {\n" +
                "      \"id\": \"8\",\n" +
                "      \"title\": \"Airplane\",\n" +
                "      \"url\": \"http://wikipedia.org....\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"edges\": [\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"1\",\n" +
                "      \"name\": \"contains\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"2\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"3\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"4\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"5\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"6\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"7\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"8\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
    }

    private class WNode {
        private final String url;
        private final String title;
        private final String id;

        WNode(String id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

        JSONObject getJSON() {
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("title", title);
            object.put("url", url);

            return object;
        }
    }

    private class WEdge {
        private final String title;
        private final String dest;
        private final String source;

        WEdge(String source, String dest, String title) {
            this.source = source;
            this.dest = dest;
            this.title = title;
        }

        JSONObject getJSON() {
            JSONObject object = new JSONObject();
            object.put("source", source);
            object.put("target", dest);
            object.put("title", title);

            return object;
        }
    }

    @RequestMapping(value = "/ggraph", method = RequestMethod.GET)
    public ResponseEntity<String> getGrabbedGraph(
            @RequestParam(value = "title") String name,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "number", required = false) Integer number) throws IOException {
        JSONObject object = new JSONObject();
        object.put("nodes", new JSONArray());
        object.put("edges", new JSONArray());


        String initialUrl = "https://ru.wikipedia.org/wiki/" + name;
        Parser parser = new Parser(initialUrl);
        parser.parse();


        object.getJSONArray("nodes").put(new WNode("0", name, initialUrl).getJSON());


        Map<String, String> cached = new HashMap<>(); // link -> id

        parseLevel(number, object, parser, cached, level == null? 1: level);



        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(object.toString(), responseHeaders, HttpStatus.CREATED);
    }

    private void parseLevel(
            @RequestParam(value = "number", required = false) Integer number,
            JSONObject object, Parser parser, Map<String, String> cached, Integer level) {
        int id = 0;
        for(Map.Entry<String, List<String>> entry: parser.getLinkMap().entrySet() ) {



            if(!cached.containsKey(entry.getKey())) {
                cached.put(entry.getKey(), String.valueOf(id));
                id ++;
            }

            String urlCandidate = entry.getKey();
            String url = urlCandidate.charAt(0) == '/' ? "https://ru.wikipedia.org/wiki" + urlCandidate: urlCandidate;


            if(level > 1) {
                try {
                    parseLevel(number, object, new Parser(url), cached, level - 1 );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(number != null) {

                String targetTitle = parseURL(url);
                targetTitle = (targetTitle == null)? " ": targetTitle;


                JSONObject newNode = new WNode(cached.get(entry.getKey()), targetTitle, url).getJSON();


                if(number > 0){
                    number -= 1;

                    object.getJSONArray("nodes").put(
                            newNode
                    );

                    String linkTitle = Util.quote(entry.getValue().get(0));
                    linkTitle.replaceAll("|", "");
//            System.out.println(linkTitle);

                    object.getJSONArray("edges")
                            .put(
                                    new WEdge("0", cached.get(entry.getKey()), linkTitle)
                                            .getJSON());
                }
            } else {


                String targetTitle = parseURL(url);
                targetTitle = (targetTitle == null)? " ": targetTitle;
//                try {
//                    parser.setUrl(url);
////                    parser.parse();
//                    targetTitle = parser.getName();
//                } catch (Exception e) {
//
//                }


                JSONObject newNode = new WNode(cached.get(entry.getKey()), targetTitle, url).getJSON();



                object.getJSONArray("nodes").put(
                        newNode
                );

                String linkTitle = Util.quote(entry.getValue().get(0));
                linkTitle.replaceAll("|", "");
//            System.out.println(linkTitle);

                object.getJSONArray("edges")
                        .put(
                                new WEdge("0", cached.get(entry.getKey()), linkTitle)
                                        .getJSON());

            }


        }
    }

    private String parseURL(String wikiUrl) {
        String result = null;
        try {
            result = URLDecoder.decode(wikiUrl,  "UTF-8");
            result = result.substring(
                    result.lastIndexOf('/') + 1 >= result.length()?
                            result.lastIndexOf('/'):
                            result.lastIndexOf('/') + 1, result.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/info")
    public ResponseEntity<String> getInfo(@RequestParam(value = "title") String name) throws IOException {
        Parser parser = new Parser( name);
        parser.parse();

        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(parser.getMainInfo(), responseHeaders, HttpStatus.CREATED);


    }
}
