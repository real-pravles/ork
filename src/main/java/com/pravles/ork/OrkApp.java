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

import com.pravles.processengine.util.ProcessDefinition;
import com.pravles.processengine.util.ProcessEngineLauncher;

import java.io.File;

import static java.lang.String.format;

public class OrkApp {
    public static void main(final String[] args) {
        final OrkApp app = new OrkApp();
        app.run(args);
    }

    void run(final String[] args) {
        if ((args == null) || (args.length != 1)) {
            System.err.println("Usage java -jar ork.jar <Zettelkasten.org>");
            System.exit(-1);
        }

        final String pathTxt = args[0];
        final File path = new File(pathTxt);

        if (!(path.exists() && path.canRead() && path.isFile())) {
            System.err.println(format(
                    "File '%s' does not exist, is not readable, " +
                    "and/or is not a file", path.getAbsolutePath()));
            System.exit(-1);
        }

        final ProcessDefinition lif =
                new MainOrkProcessDefinition(path.getAbsolutePath());
        new ProcessEngineLauncher().run(lif);
    }
}
