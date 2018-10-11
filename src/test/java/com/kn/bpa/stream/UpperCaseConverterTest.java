package com.kn.bpa.stream;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class UpperCaseConverterTest {

    @Test
    public void testConvertAllToUpperCase() {
        List<String> expected = Arrays.asList("FOO", "BAR");
        List<String> result = UpperCaseConverter.convertAllToUpperCase(Arrays.asList("foo", "bar"));
        assertEquals(expected, result);
    }

    @Test
    @Ignore
    public void testConvertFirstLetterUpperCase() {
        List<String> expected = Arrays.asList("Foo", "Bar");
        List<String> result = UpperCaseConverter.convertFirstCharToUpperCase(Arrays.asList("foo", "bar"));
        assertEquals(expected, result);
    }
}