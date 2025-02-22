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

/**
 * Interface that defines a configuration that can be supplied via different sources.
 *
 * @author Marcelo Guimarães
 */
public interface Configuration extends InputValue {

  /**
   * Represents a not supplied configuration.
   */
  Configuration NOT_SUPPLIED = new NotSuppliedConfiguration();

  /**
   * Checks if this source has some configuration set.
   *
   * @return {@code true} if this source holds any configuration value.
   */
  boolean isSet();

  default Configuration ifSet(Consumer<Configuration> action) {
    if (isSet()) {
      action.accept(this);
    }
    return this;
  }

  /**
   * Assumes this configuration value is pointing to an external location
   * and reads the value at that location.
   *
   * @return the value stored in the location defined by this configuration.
   */
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

  /**
   * Assumes this configuration value is pointing to an external location
   * and reads the lines at that location.
   *
   * @return the lines stored in the location defined by this configuration.
   */
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

  /**
   * Returns this configuration if it's set or the given default
   * configuration otherwise.
   *
   * @param defaultConfiguration the default configuration to return
   * @return this configuration or the default one
   */
  default Configuration or(Configuration defaultConfiguration) {
    return isSet() ? this : defaultConfiguration;
  }

  static ConfigurationChain configuration() {
    return new ConfigurationChain(new NotSuppliedConfiguration());
  }

  static Configuration env(String key) {
    return new EnvironmentVariableConfiguration(key);
  }

  static Configuration file(String key) {
    return new FileConfiguration(key);
  }

  static Configuration resource(ClassLoader classLoader, String key) {
    return new ResourceConfiguration(classLoader, key);
  }

  static Configuration resource(String key) {
    return new ResourceConfiguration(Thread.currentThread().getContextClassLoader(), key);
  }

  static Configuration property(String key) {
    return new SystemPropertyConfiguration(key);
  }

  static Configuration url(String key) {
    return new UrlConfiguration(key);
  }

  static Configuration value(String key) {
    return new RawValueConfiguration(key);
  }

}
