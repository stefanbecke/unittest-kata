package com.kn.bpa.stream;

import java.util.List;
import java.util.stream.Collectors;

public class UpperCaseConverter {

   public static List<String> convertAllToUpperCase(List<String> words) {
        return words.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

    public static List<String> convertFirstCharToUpperCase(List<String> words) {
        return words;
    }
}
