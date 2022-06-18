package com.mailsender.utils;

import java.io.UnsupportedEncodingException;

public class StringEncoder {

    public static String encodeUTF8(String msg){
        try{
            return new String(msg.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e){
            return "Can't encode";
        }
    }
}
