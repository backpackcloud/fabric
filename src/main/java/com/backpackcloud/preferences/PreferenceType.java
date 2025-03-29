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

package com.backpackcloud.preferences;

import com.backpackcloud.UnbelievableException;

import java.util.function.Function;

/// Represents a type that can be assigned to a preference.
///
/// @param name      the name of this type
/// @param converter how to convert the user input
/// @author Ataxexe
public record PreferenceType<E>(String name,
                                Function<String, E> converter) {

  /// Converts an input text into a value that this type of preference can hold.
  ///
  /// @param input the input value to convert.
  /// @return the converted value.
  public E convert(String input) {
    return converter.apply(input);
  }

  /// A built-in preference that holds any String value.
  public static final PreferenceType<String> TEXT = new PreferenceType<>("text", Function.identity());

  /// A built-in preference that holds boolean values.
  ///
  /// It supports the forms `true/false`, `on/off` and `yes/no`.
  public static final PreferenceType<Boolean> FLAG = new PreferenceType<>("flag", input ->
    switch (input) {
      case "on", "true", "yes" -> true;
      case "off", "false", "no" -> false;
      default -> throw new UnbelievableException("Invalid input!");
    });

  /// A built-in preference that holds any Integer value.
  public static final PreferenceType<Integer> NUMBER = new PreferenceType<>("number", Integer::parseInt);

  /// A built-in preference that holds any Double value.
  public static final PreferenceType<Double> DECIMAL = new PreferenceType<>("decimal", Double::parseDouble);


}
