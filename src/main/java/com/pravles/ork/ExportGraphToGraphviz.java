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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jgrapht.Graph;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.joining;

@Slf4j
public class ExportGraphToGraphviz implements com.pravles.processengine.api.ActivityFunction {

    private static final double MIN_SIZE = 0.3;
    private static final double MAX_SIZE = 2;
    public static final String NL = System.lineSeparator();
    public static final int TITLE_WIDTH = 50;

    @Override
    public Map<String, Object> apply(Map<String, Object> ctx) {
        final Graph<String, OrkEdge> graph = (Graph<String, OrkEdge>) ctx.get("graph");
        final Map<String, Map<String, Object>> notes = (Map<String, Map<String, Object>>) ctx.get("notes");
        final String mainZkPath = (String) ctx.get("main-zk-path");

        final StringBuilder sb = new StringBuilder();
        sb.append("graph G {");
        sb.append(NL);


        sb.append(asList("graph", "node", "edge")
                        .stream()
                        .map(type ->
                                format("  %s [fontname=\"Courier Prime\"]",
                                        type))
                        .collect(joining(NL)));
        sb.append(NL);
        sb.append("  overlap=false");
        sb.append(NL);

        final List<String> nodeIds = new ArrayList<>(notes.keySet());
        sort(nodeIds);
        final Integer maxDegree = nodeIds
                .stream()
                .map(graph::degreeOf)
                .max(Integer::compareTo)
                .orElse(1);

        sb.append(
                nodeIds.stream()
                        .map(nodeId -> renderNode(nodeId, notes, maxDegree, graph))
                        .collect(joining()));
        sb.append(
                graph.edgeSet().stream()
                        .map(edge -> renderEdge(edge))
                        .collect(joining()));

        sb.append("}");
        sb.append(NL);

        writeToFile(mainZkPath, sb);

        return ctx;
    }

    private String renderEdge(final OrkEdge edge) {
        return format("  \"%s\" -- \"%s\"%s",
                edge.getSource(),
                edge.getTarget(),
                NL);
    }

    private String renderNode(final String nodeId,
                              final Map<String, Map<String, Object>> notes, Integer maxDegree, Graph<String, OrkEdge> graph) {

        final int degree = graph.degreeOf(nodeId);

        double size =
                MIN_SIZE +
                        (MAX_SIZE - MIN_SIZE)
                                * Math.sqrt((double) degree / maxDegree);

        final Map<String, Object> note = notes.get(nodeId);
        final String title = (String) note.get("title");


        final StringBuilder sb = new StringBuilder();
        sb.append("<b>");
        sb.append(nodeId);
        sb.append("</b>");
        if (StringUtils.isNotBlank(title)) {
            sb.append("<br/>");
            sb.append(WordUtils.wrap(title, TITLE_WIDTH, "<br/>", true));
        }

        return format("  \"%s\" [label=<%s>, shape=ellipse, width=%.2f, height=%.2f]%s",
                nodeId, sb.toString(), 1.5*size, 1.0*size, NL);
    }

    private static void writeToFile(String mainZkPath, StringBuilder sb) {
        final File dotFile = composeDotFileName(mainZkPath);

        try {
            FileUtils.writeStringToFile(dotFile, sb.toString(), Charset.forName("UTF-8"));
        } catch (final IOException e) {
            log.error(format("An error occurred while trying to write to file '%s'",
                    dotFile.getAbsolutePath()), e);
        }
    }

    private static File composeDotFileName(String mainZkPath) {
        final File mainZkFile = new File(mainZkPath);
        final File parent = mainZkFile.getParentFile();
        final String mainZkName = mainZkFile.getName();

        final File dotFile = new File(format("%s/%s.dot",
                parent.getAbsolutePath(), mainZkName));
        return dotFile;
    }
}
