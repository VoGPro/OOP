package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CarJsonRepository implements IFileStrategy {

    @Override
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}