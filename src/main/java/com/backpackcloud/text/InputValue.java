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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalQuery;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/// Interface that abstracts a value that is external to the source code, provided as a String
/// (like a configuration parameter).
///
/// This interface aims to be a simplistic way of handling such values.
///
/// @author Ataxexe
public interface InputValue extends Supplier<String> {

  /// Gets this input value as it is. Might be {@code null}.
  ///
  /// @see #asText()
  @JsonValue
  @Override
  String get();

  /// Wraps the input value in an Optional.
  ///
  /// Empty Strings or `null` values yield {@link Optional#isEmpty() empty} optionals.
  ///
  /// @return the input value wrapped in an Optional object.
  default Optional<String> asText() {
    String value = get();
    return value == null || value.isEmpty() ? Optional.empty() : Optional.of(value);
  }

  /// Short to {@code asText().map(mapper)}
  default <T> Optional<T> map(Function<String, T> mapper) {
    return asText().map(mapper);
  }

  /// Converts the {@link #asText() text} value to an Integer object.
  ///
  /// An exception while converting will result in an empty Optional.
  ///
  /// @return the result of the conversion.
  /// @see Integer#parseInt(String)
  default Optional<Integer> asInteger() {
    try {
      return map(Integer::parseInt);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  /// Converts the {@link #asText() text} value to a Long object.
  ///
  /// An exception while converting will result in an empty Optional.
  ///
  /// @return the result of the conversion.
  /// @see Long#parseLong(String)
  default Optional<Long> asLong() {
    try {
      return map(Long::parseLong);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  /// Converts the {@link #asText() text} value to a Double object.
  ///
  /// An exception while converting will result in an empty Optional.
  ///
  /// @return the result of the conversion.
  /// @see Double#parseDouble(String)
  default Optional<Double> asDouble() {
    try {
      return map(Double::parseDouble);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  /// Converts the {@link #asText() text} value to a Boolean object.
  ///
  /// An exception while converting will result in an empty Optional.
  ///
  /// @return the result of the conversion.
  /// @see Boolean#parseBoolean(String)
  default Optional<Boolean> asBoolean() {
    return map(Boolean::parseBoolean);
  }

  /// Converts the {@link #asText() text} value to a Temporal object defined by the
  /// provided pattern and TemporalQuery.
  ///
  /// An exception while converting will result in an empty Optional.
  ///
  /// @return the result of the conversion
  /// @see DateTimeFormatter#ofPattern(String)
  /// @see DateTimeFormatter#parse(CharSequence, TemporalQuery)
  default <T> Optional<T> asTemporal(String pattern, TemporalQuery<T> query) {
    try {
      return map(input -> DateTimeFormatter.ofPattern(pattern).parse(input, query));
    } catch (IllegalArgumentException | DateTimeParseException e) {
      return Optional.empty();
    }
  }

  /// Converts the {@link #asText() text} value to an Enum object defined by the
  /// provided enum class.
  ///
  /// In order to increase the chances of a successful conversion, the following steps are
  /// applied prior to the conversion:
  ///
  /// - The text is converted to upper case
  /// - Any occurrences of "-", " ", "." will be replaced by "_"
  ///
  /// An exception while converting will result in an empty Optional.
  ///
  /// @return the result of the conversion
  /// @see Enum#valueOf(Class, String)
  default <T extends Enum<T>> Optional<T> asEnum(Class<T> enumType) {
    try {
      return map(String::toUpperCase)
        .map(input -> input.replaceAll("([- .])", "_"))
        .map(input -> Enum.valueOf(enumType, input));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  /// Splits this InputValue in multiple instances by treating the value as a comma-separated
  /// array of values.
  ///
  /// @return A stream of each input value
  default Stream<InputValue> split() {
    return split(s -> s.split("\\s*,\\s*"));
  }

  /// Splits this InputValue in multiple instances by splitting its value using the given function.
  ///
  /// @param function the function to split the value
  /// @return A stream of each input value
  default Stream<InputValue> split(Function<String, String[]> function) {
    return Arrays.stream(function.apply(get()))
      .map(InputValue::of);
  }

  /// Creates a basic input value from the provided value.
  ///
  /// @param input the actual input value
  /// @return a new InputValue instance
  @JsonCreator
  static InputValue of(String input) {
    return () -> input;
  }

  /// Creates an input value around the given supplier.
  ///
  /// @param input the supplier to wrap into an InputValue
  /// @return a new InputValue instance
  static InputValue of(Supplier<String> input) {
    return input::get;
  }

  /// Represents an empty InputValue
  InputValue EMPTY = () -> null;

}
