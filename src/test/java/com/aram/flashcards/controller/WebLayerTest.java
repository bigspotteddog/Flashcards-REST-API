package com.aram.flashcards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract class WebLayerTest {

    private final ObjectMapper objectMapper;


    public WebLayerTest() {
        this.objectMapper = new ObjectMapper();
    }

    String serialize(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}
