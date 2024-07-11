package com.alurachallegenge.lite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConvertData implements iConvertData {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    //public <T> T getData(String url, Class<T> tClass){
    public <T> T getData(String url, Class<T> tClass) {

        try {

            return objectMapper.readValue(url, tClass);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
