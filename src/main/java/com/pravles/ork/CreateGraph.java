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

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.Pseudograph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateGraph implements com.pravles.processengine.api.ActivityFunction {
    private final static ExtractNoteData extractNodeData = new ExtractNoteData();

    @Override
    public Map<String, Object> apply(Map<String, Object> ctx) {
        final List<String> noteTxts = (List<String>) ctx.get("note-txts");

        final Map<String, Map<String, Object>> notes =
                noteTxts
                        .stream()
                        .map(txt -> extractNodeData.apply(txt))
                        .collect(Collectors.toMap(
                                m -> (String) m.get("id"),
                                m -> m
                        ));

        final Graph<String, DefaultEdge> graph =
                new Pseudograph<>(DefaultEdge.class);

        ctx.put("graph", graph);

        return ctx;
    }
}
