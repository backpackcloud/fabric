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
