package com.pravles;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestUtils {

    private TestUtils() {
    }

    public static void assertFilesEqual(final File expectedFile,
                                        final File actualFile)
            throws IOException {
        final String expected =
                readFileToString(expectedFile,
                        "UTF-8");
        final String actual = readFileToString(actualFile,
                "UTF-8");
        final String msg = format("expected file: '%s', actual file: '%s'",
                expectedFile.getAbsolutePath(),
                actualFile.getAbsolutePath());
        assertEquals(expected, actual, msg);
        assertTrue(FileUtils.contentEquals(expectedFile, actualFile), msg);
    }
}
