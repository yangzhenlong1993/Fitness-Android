package com.zhenlong.Fitness.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.zhenlong.Fitness.Bean.Msg;

import java.io.IOException;

public class JsonTransfer {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static Msg JsonToMsg(String json) {
        Msg msg;
        try {
            msg = OBJECT_MAPPER.readValue(json, Msg.class);
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String MsgToJson(Msg msg) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(msg);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } finally {

        }

    }
}
