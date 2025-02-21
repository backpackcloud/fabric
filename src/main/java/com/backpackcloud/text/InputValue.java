/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 Marcelo "Ataxexe" Guimarães
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.backpackcloud.text;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface InputValue extends Supplier<String> {

  @Override
  String get();

  default Optional<String> text() {
    String value = get();
    return value == null || value.isEmpty() ? Optional.empty() : Optional.of(value);
  }

  default <T> Optional<T> map(Function<String, T> mapper) {
    return text().map(mapper);
  }

  default Optional<Integer> integer() {
    try {
      return map(Integer::parseInt);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  default Optional<Long> number() {
    try {
      return map(Long::parseLong);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  default Optional<Double> decimal() {
    try {
      return map(Double::parseDouble);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  default Optional<Boolean> bool() {
    return map(Boolean::parseBoolean);
  }

  default <T> Optional<T> temporal(String pattern, TemporalQuery<T> query) {
    return map(input -> DateTimeFormatter.ofPattern(pattern).parse(input, query));
  }

  default <T extends Enum<T>> Optional<T> identify(Class<T> enumType) {
    try {
      return map(String::toUpperCase)
        .map(input -> input.replaceAll("([- .])", "_"))
        .map(input -> Enum.valueOf(enumType, input));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  default Stream<InputValue> split() {
    return Arrays.stream(get().split("\\s*,\\s*"))
      .map(InputValue::of);
  }

  static InputValue of(String input) {
    return () -> input;
  }

  static InputValue of(Supplier<String> input) {
    return input::get;
  }

}
