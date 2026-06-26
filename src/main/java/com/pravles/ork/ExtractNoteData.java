/*
 * Copyright 2026 Pravles Redneckoff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the “Software”), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.pravles.ork;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractNoteData implements Function<String, Map<String, Object>> {

    private static final Pattern ID_PATTERN =
            Pattern.compile("<<n([^>]+)>>");

    private static final Pattern HEADER_PATTERN =
            Pattern.compile("^\\*\\*\\s+([^\\s]+)\\s+\\(([^)]+)\\)(?::\\s*(.*))?$",
                    Pattern.MULTILINE);

    private static final Pattern LINK_PATTERN =
            Pattern.compile("\\[\\[n([A-Za-z0-9.]+)(?:\\]\\[[^\\]]+)?\\]\\]");

    @Override
    public Map<String, Object> apply(final String input) {
        final String id = extractId(input);

        return Map.of(
                "id", id,
                "title", extractTitle(input),
                "timestamp", extractTimestamp(input),
                "linked-notes", extractLinkedNotes(input),
                "train-of-thought", extractTrainOfThought(id)
        );
    }

    private List<String> extractTrainOfThought(final String id) {
        if (!id.contains(".")) {
            return Collections.singletonList(id);
        }

        final List<String> result = new ArrayList<>();

        StringBuilder currentId = new StringBuilder();
        boolean curIdNumeric = StringUtils.isNumeric(id);

        boolean dotFound = false;

        for (int i=0; i < id.length(); i++) {
            final char curChar = id.charAt(i);
            final boolean curCharNumeric = CharUtils.isAsciiNumeric(curChar);

            if (".".equals(curChar)) {
                result.add(currentId.toString());
                currentId.setLength(0);
            } else if (curIdNumeric == curCharNumeric) {
                currentId.append(curChar);
                curIdNumeric = curCharNumeric;
            } else {
                result.add(currentId.toString());
                currentId.setLength(0);
                currentId.append(curChar);
                curIdNumeric = curCharNumeric;
            }
        }

        /*

        while (!current.isEmpty() && !dotFound) {
            result.add(current);

            current = current.substring(0, current.length() - 1);

            if (current.endsWith(".")) {
                result.add(current.substring(0, current.length() - 1));
                dotFound = true;
            }
        }

         */

        Collections.sort(result);

        return result;
    }

    private Set<String> extractLinkedNotes(final String input) {
        final Matcher matcher = LINK_PATTERN.matcher(input);

        final Set<String> result = new HashSet<>();

        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }

    private String extractTimestamp(final String input) {
        final Matcher matcher = HEADER_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Cannot extract timestamp");
        }

        return matcher.group(2);
    }

    private String extractTitle(final String input) {
        final Matcher matcher = HEADER_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Cannot extract title");
        }

        final String title = matcher.group(3);

        return title == null ? "" : title;
    }

    private String extractId(final String input) {
        final Matcher matcher = ID_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Cannot extract id");
        }

        return matcher.group(1);
    }
}