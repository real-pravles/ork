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
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.Pseudograph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        final Graph<String, OrkEdge> graph =
                new Pseudograph<>(OrkEdge.class);

        final List<String> noteIds = new ArrayList<>(notes.keySet());
        Collections.sort(noteIds);

        for (final String id : noteIds) {
            if (!graph.containsVertex(id)) {
                graph.addVertex(id);
            }
            final Map<String, Object> note = notes.get(id);
            for (final String linkedNoteId : (Set<String>) note.get("linked-notes")) {
                if (!graph.containsEdge(id, linkedNoteId)) {
                    if (!graph.containsVertex(linkedNoteId)) {
                        graph.addVertex(linkedNoteId);
                    }
                    graph.addEdge(id, linkedNoteId);
                }
            }
            final List<String> trainOfThought = (List<String>) note.get("train-of-thought");
            if (trainOfThought.size() > 1) {
                int i = 1;

                while (i < trainOfThought.size()) {
                    final String source = trainOfThought.get(i-1);
                    final String target = trainOfThought.get(i);

                    if (!graph.containsEdge(source, target)) {
                        graph.addEdge(source, target);
                    }

                    i++;
                }
            }
        }

        ctx.put("graph", graph);
        ctx.put("notes", notes);

        return ctx;
    }
}
