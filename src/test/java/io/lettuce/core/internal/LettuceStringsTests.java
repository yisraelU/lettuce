/*
 * Copyright 2020-Present, Redis Ltd. and Contributors
 * All rights reserved.
 *
 * Licensed under the MIT License.
 *
 * This file contains contributions from third-party contributors
 * licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.internal;

import static io.lettuce.TestTags.UNIT_TEST;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Kevin McLaughlin
 */
@Tag(UNIT_TEST)
class LettuceStringsTests {

    @Test
    void testToDoubleNan() {
        assertThat(LettuceStrings.toDouble("-nan")).isNaN();
        assertThat(LettuceStrings.toDouble("nan")).isNaN();
        assertThat(LettuceStrings.toDouble("+nan")).isNaN();
    }

    @Test
    void testToDoubleInf() {
        assertThat(LettuceStrings.toDouble("-inf")).isEqualTo(Double.NEGATIVE_INFINITY);
        assertThat(LettuceStrings.toDouble("inf")).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(LettuceStrings.toDouble("+inf")).isEqualTo(Double.POSITIVE_INFINITY);
    }

}
