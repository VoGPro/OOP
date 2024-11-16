package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class CarYamlRepository implements IFileStrategy {

    @Override
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }
}