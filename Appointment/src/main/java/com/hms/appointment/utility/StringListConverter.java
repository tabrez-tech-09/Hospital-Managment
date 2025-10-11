package com.hms.appointment.utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringListConverter {

    public static String convertedListToString(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }

    public static List<String> convertedStringToList(String str) {
        if (str == null || str.trim().isEmpty()) return Collections.emptyList();
        return Arrays.stream(str.split(","))
                     .map(String::trim)
                     .collect(Collectors.toList());
    }
}

