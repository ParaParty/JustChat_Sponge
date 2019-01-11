package com.superexercisebook.justchat;

import java.nio.charset.Charset;
import java.util.Base64;

public class MessageTools {
    static String Base64Decode(String s){
        return new String(Base64.getDecoder().decode(s), Charset.forName("UTF-8"));
    }

    static String Base64Encode(String s){
        return Base64.getEncoder().encodeToString(s.getBytes(Charset.forName("utf-8")));
    }
}
