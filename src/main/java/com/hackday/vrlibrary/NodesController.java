package com.hackday.vrlibrary;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by maxim on 26.03.2016.
 */
@RestController
public class NodesController {


    @RequestMapping(value = "/graph", method = RequestMethod.GET)
    public String getGraph(@RequestParam(value = "title") String name) {


        return "{\n" +
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
                "    }\n" +
                "  ],\n" +
                "  \"edges\": [\n" +
                "    {\n" +
                "      \"source\": \"0\",\n" +
                "      \"target\": \"1\",\n" +
                "      \"name\": \"contains\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": \"2\",\n" +
                "      \"target\": \"0\",\n" +
                "      \"name\": \"For great glory of \"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

    }

}
