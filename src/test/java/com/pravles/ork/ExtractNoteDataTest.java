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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExtractNoteDataTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void shouldExtractNoteData(String input, Map<String, Object> expected) {
        // when
        Map<String, Object> actual = ExtractNoteData.extract(input);

        // then
        assertEquals(expected, actual);
    }

    static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        """
                        ** 1 (2026-06-08 23:32)
                        <<n1>>
        
                        How can we improve this whole story?
                        """,
                        Map.of(
                                "id", "n1",
                                "title", "",
                                "timestamp", "2026-06-08 23:32",
                                "linked-notes", Collections.emptyList()
                        )
                ),
                Arguments.of(
                        """
                        ** 2 (2026-06-12 15:03)
                        <<n2>>
        
                        Can we use the material from the analog ZK?
                        """,
                        Map.of(
                                "id", "n2",
                                "title", "",
                                "timestamp", "2026-06-12 15:03",
                                "linked-notes", Collections.emptyList()
                        )
                ),
                Arguments.of(
                        """
** 3 (2026-06-13 10:49)
<<n3>>

Here is an idea for a plot tool: An invention, some machine that allows to aggregate people's dissent with the government and make sure the government gets the negative feedback.

Related notes:

 * [[n4][4]]
 * [[n5][5]]
 * [[n11][11]]: How can we make the device appealing to the reader?
 * [[n9][9]]: Subsystems of the device
 * [[n14][14]]: Forms of the device
 * [[n15][15]]: Mobile app (social network)
 * [[n22][22]]: Building a story around the device
                        """,
                        Map.of(
                                "id", "3",
                                "title", "",
                                "timestamp", "2026-06-13 10:49",
                                "linked-notes", Arrays.asList("4", "5",
                                        "11", "9", "14", "15", "22")
                        )
                ),
                Arguments.of(
                        """
** 81.a1b (2026-06-22 19:57): Skeleton of a story of decision
<<n81.a1b>>

We have a skeleton for a story of decision:

1. Actor: OSINT guy, A foreign hacker who managed to get access to one of the implants (in a living person and the person does not know about it).

2. What is the decision about? To use his power over the victim or not to use the power over the victim.

3. If he livestreams the experiments he performs on this person in realtime, he may get rich.

4. If he does not use this opportunity to experiment on a live human, it may never come back.

See

 * [[n24][24]]
 * [[n80]]                                
                        """,
                        Map.of(
                                "id", "81.a1b",
                                "title", "Skeleton of a story of decision",
                                "timestamp", "2026-06-22 19:57",
                                "linked-notes", Arrays.asList("24",
                                        "80")
                        )
                ),

        );
    }
}