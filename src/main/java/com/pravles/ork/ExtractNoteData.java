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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExtractNoteData implements Function<String, Map> {
    @Override
    public Map apply(final String input) {
        final String id = extractId(input);

        return Map.of(
                "id", id,
                "title", extractTitle(input),
                "timestamp", extractTimestamp(input),
                "linked-notes", extractLinkedNotes(input),
                "train-of-thought", extractTrainOfThought(id)
        );
    }

    private List<String> extractTrainOfThought(String id) {
        return null;
    }

    private List<String> extractLinkedNotes(String input) {
        return null;
    }

    private String extractTimestamp(String input) {
        return null;
    }

    private String extractTitle(String input) {
        return null;
    }

    private String extractId(String input) {
        return null;
    }
}
