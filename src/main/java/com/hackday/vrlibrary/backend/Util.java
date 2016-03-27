package com.hackday.vrlibrary.backend;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by maxim on 27.03.2016.
 */
public class Util {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String url =
                "https://ru.wikipedia.org/wiki/wiki/%D0%94%D0%BE%D0%BB%D0%B8%D0%B2%D0%BE-" +
                        "%D0%94%D0%BE%D0%B1%D1%80%D0%BE%D0%B2%D0%BE%D0%BB%D1%8C%D1%81%D0%BA%D0%B8%D0%B9" +
                        ",_%D0%9C%D0%B8%D1%85%D0%B0%D0%B8%D0%BB_%D0%9E%D1%81%D0%B8%D0%BF%D0%BE%D0%B2%D0%B8%D1%87";


        String result = URLDecoder.decode(url,  "UTF-8");
        System.out.println(result.substring(result.lastIndexOf('/') + 1, result.length()));


    }


    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    //                if (b == '<') {
                    sb.append('\\');
                    //                }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }
}
