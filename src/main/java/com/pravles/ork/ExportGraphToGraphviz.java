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
import org.jgrapht.Graph;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ExportGraphToGraphviz implements com.pravles.processengine.api.ActivityFunction {

    public static final String NL = System.lineSeparator();

    @Override
    public Map<String, Object> apply(Map<String, Object> ctx) {
        final Graph<String, OrkEdge> graph = (Graph<String, OrkEdge>) ctx.get("graph");
        final Map<String, Map<String, Object>> notes = (Map<String, Map<String, Object>>) ctx.get("notes");
        final String mainZkPath = (String) ctx.get("main-zk-path");

        final StringBuilder sb = new StringBuilder();
        sb.append("graph G {");
        sb.append(NL);

        final List<String> nodeIds = new ArrayList<>(notes.keySet());
        Collections.sort(nodeIds);

        sb.append(
                nodeIds.stream()
                        .map(nodeId -> renderNode(nodeId, notes))
                        .collect(Collectors.joining())

        );

        sb.append(
                graph.edgeSet().stream()
                        .map(edge -> renderEdge(edge))

                        .collect(Collectors.joining())

        );

        sb.append("}");
        sb.append(NL);



        writeToFile(mainZkPath, sb);

        return ctx;
    }

    private String renderEdge(final OrkEdge edge) {
        return String.format("  \"%s\" -- \"%s\"%s",
                edge.getSource(),
                edge.getTarget(),
                NL);
    }

    private String renderNode(final String nodeId, final Map<String, Map<String, Object>> notes) {
        return String.format("  \"%s\" [label=\"%s\"]%s",
                nodeId, nodeId, NL);
    }

    private static void writeToFile(String mainZkPath, StringBuilder sb) {
        final File dotFile = composeDotFileName(mainZkPath);

        try {
            FileUtils.writeStringToFile(dotFile, sb.toString(), Charset.forName("UTF-8"));
        } catch (final IOException e) {
            log.error(String.format("An error occurred while trying to write to file '%s'",
                    dotFile.getAbsolutePath()), e);
        }
    }

    private static File composeDotFileName(String mainZkPath) {
        final File mainZkFile = new File(mainZkPath);
        final File parent = mainZkFile.getParentFile();
        final String mainZkName = mainZkFile.getName();

        final File dotFile = new File(String.format("%s/%s.dot",
                parent.getAbsolutePath(), mainZkName));
        return dotFile;
    }
}
