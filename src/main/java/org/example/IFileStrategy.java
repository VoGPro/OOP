package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface IFileStrategy {
    ObjectMapper createObjectMapper();
}
