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
    private static final ExtractNoteData sut = new ExtractNoteData();

    @ParameterizedTest
    @MethodSource("testCases")
    void shouldExtractNoteData(String input, Map<String, Object> expected) {
        // when
        final Map<String, Object> actual = sut.apply(input);

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
                                "id", "1",
                                "title", "",
                                "timestamp", "2026-06-08 23:32",
                                "linked-notes", Collections.emptyList(),
                                "train-of-thought", Arrays.asList("1")
                        )
                ),
                Arguments.of(
                        """
                        ** 2 (2026-06-12 15:03)
                        <<n2>>
        
                        Can we use the material from the analog ZK?
                        """,
                        Map.of(
                                "id", "2",
                                "title", "",
                                "timestamp", "2026-06-12 15:03",
                                "linked-notes", Collections.emptyList(),
                                "train-of-thought", Arrays.asList("2")
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
                                        "11", "9", "14", "15", "22"),
                                "train-of-thought", Collections.emptyList()
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
                                        "80"),
                                "train-of-thought", Collections.emptyList()
                        )
                ),
                Arguments.of(
                        """
** 80.3 (2026-06-15 22:07): What is the decision about?
<<n80.3>>

Re [[n80.1a][80.1a]]: What is the decision about?

See [[n80.1a][80.1a]]
                        """,
                        Map.of(
                                "id", "80.3",
                                "title", "What is the decision about?",
                                "timestamp", "2026-06-15 22:07",
                                "linked-notes", Arrays.asList("80.1a"),
                                "train-of-thought", Collections.emptyList()
                        )
                ),
                Arguments.of(
                        """
** 74.1a2 (2026-06-16 11:58): The fiancée left the Roskompozor guy
<<n74.1a2>>                        
                        """,
                        Map.of(
                                "id", "74.1a2",
                                "title", "The fiancée left the Roskompozor guy",
                                "timestamp", "2026-06-16 11:58",
                                "linked-notes", Collections.emptyList(),
                                "train-of-thought", Arrays.asList("74", "74.1", "74.1a", "74.1a2")
                        )
                )
                );
    }
}