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

package com.backpackcloud.configuration;

import com.backpackcloud.UnbelievableException;
import com.backpackcloud.text.InputValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/// Interface that defines a configuration that can be supplied via different sources.
///
/// @author Ataxexe
public interface Configuration extends InputValue {

  /// Represents a not supplied configuration. Basically a `null` object.
  Configuration NOT_SUPPLIED = new NotSuppliedConfiguration();

  /// Checks if this source has some configuration set.
  ///
  /// @return `true` if this source holds any configuration value.
  boolean isSet();

  default Configuration ifSet(Consumer<Configuration> action) {
    if (isSet()) {
      action.accept(this);
    }
    return this;
  }

  /// Assumes this configuration value is pointing to an external location
  /// and reads the value at that location.
  ///
  /// @return the value stored in the location defined by this configuration.
  default String read() {
    try {
      Path path = asText()
        .map(Path::of)
        .orElseThrow();
      return Files.readString(path);
    } catch (IOException e) {
      throw new UnbelievableException(e);
    }
  }

  /// Assumes this configuration value is pointing to an external location
  /// and reads the lines at that location.
  ///
  /// @return the lines stored in the location defined by this configuration.
  default List<String> readLines() {
    try {
      Path path = asText()
        .map(Path::of)
        .orElseThrow();
      return Files.readAllLines(path);
    } catch (IOException e) {
      throw new UnbelievableException(e);
    }
  }

  /// Returns this configuration if it's set or the given default
  /// configuration otherwise.
  ///
  /// @param defaultConfiguration the default configuration to return
  /// @return this configuration or the default one
  default Configuration or(Configuration defaultConfiguration) {
    return isSet() ? this : defaultConfiguration;
  }

  /// A convenience method for starting a configuration chain which defaults to {@link #NOT_SUPPLIED}.
  static ConfigurationChain configuration() {
    return new ConfigurationChain(NOT_SUPPLIED);
  }

  /// Creates and returns a configuration that uses the value stored in an environment variable
  /// based on the given name.
  ///
  /// @param name the environment variable name
  /// @return a new Configuration object
  /// @see EnvironmentVariableConfiguration
  static Configuration env(String name) {
    return new EnvironmentVariableConfiguration(name);
  }

  /// Creates and returns a configuration that uses the file located at the given location.
  ///
  /// @param location the file location
  /// @return a new Configuration object
  /// @see FileConfiguration
  static Configuration file(String location) {
    return new FileConfiguration(location);
  }

  /// Creates and returns a configuration that uses the resource located at the given location.
  ///
  /// @param location the resource location
  /// @return a new Configuration object
  /// @see ResourceConfiguration
  static Configuration resource(String location) {
    return new ResourceConfiguration(location);
  }

  /// Creates and returns a configuration that uses the value stored in a system property
  /// based on the given name.
  ///
  /// @param name the name of the system property
  /// @return a new Configuration object
  /// @see SystemPropertyConfiguration
  static Configuration property(String name) {
    return new SystemPropertyConfiguration(name);
  }

  /// Creates and returns a configuration that points to the given url.
  ///
  /// @param location the url location
  /// @return a new Configuration object
  /// @see UrlConfiguration
  static Configuration url(String location) {
    return new UrlConfiguration(location);
  }

  /// Creates and returns a configuration that only holds the given value.
  ///
  /// @param value the value of the configuration
  /// @return a new Configuration object
  /// @see RawValueConfiguration
  static Configuration value(String value) {
    return new RawValueConfiguration(value);
  }

}
