package com.example.infrastructure.util;

import org.apache.commons.codec.digest.DigestUtils;
import java.util.List;
import java.util.stream.Collectors;

public class HashGenerator {
    
    public static String generateMd5Hash(List<String> values) {
        String concatenatedValues = values.stream()
                .filter(value -> value != null)
                .collect(Collectors.joining("_"));
        return DigestUtils.md5Hex(concatenatedValues);
    }
} 