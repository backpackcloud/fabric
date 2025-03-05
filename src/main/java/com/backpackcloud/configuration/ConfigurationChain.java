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

import java.util.List;

/// A class that acts as a Configuration, but provides mechanisms to chain the
/// configuration using other configurations.
///
/// Essentially, the first configuration in the chain is the highest priority.
/// Any other configuration chained will be used only if the previous one
/// is {@link Configuration#isSet() not set}.
///
/// This class is immutable, so all chaining methods will return a new object.
///
/// @author Ataxexe
public class ConfigurationChain implements Configuration {

  /// The actual Configuration this class will delegate the interface methods.
  private final Configuration configuration;

  /// Creates a new chain using the given configuration as the first priority.
  ///
  /// If the given configuration is already a chain, its priority will be taken
  /// into account automatically.
  ///
  /// @param configuration the configuration to create the chain, might be already a chain
  public ConfigurationChain(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public boolean isSet() {
    return configuration.isSet();
  }

  @Override
  public String get() {
    return configuration.get();
  }

  @Override
  public String read() {
    return configuration.read();
  }

  @Override
  public List<String> readLines() {
    return configuration.readLines();
  }

  @Override
  public Configuration or(Configuration defaultConfiguration) {
    return configuration.or(defaultConfiguration);
  }

  /// Chains this configuration with an environment configuration.
  ///
  //// @return a new chain object
  /// @see Configuration#env(String)
  public ConfigurationChain env(String name) {
    return new ConfigurationChain(configuration.or(Configuration.env(name)));
  }

  /// Chains this configuration with a file configuration.
  ///
  /// @return a new chain object
  /// @see Configuration#file(String)
  public ConfigurationChain file(String location) {
    return new ConfigurationChain(configuration.or(Configuration.file(location)));
  }

  /// Chains this configuration with a resource configuration.
  ///
  /// @return a new chain object
  /// @see Configuration#resource(String)
  public ConfigurationChain resource(String location) {
    return new ConfigurationChain(configuration.or(Configuration.resource(location)));
  }

  /// Chains this configuration with a property configuration.
  ///
  /// @return a new chain object
  /// @see Configuration#property(String)
  public ConfigurationChain property(String name) {
    return new ConfigurationChain(configuration.or(Configuration.property(name)));
  }

  /// Chains this configuration with a url configuration.
  ///
  /// @return a new chain object
  /// @see Configuration#url(String)
  public ConfigurationChain url(String location) {
    return new ConfigurationChain(configuration.or(Configuration.url(location)));
  }

  /// Chains this configuration with a value configuration.
  ///
  /// @return a new chain object
  /// @see Configuration#value(String)
  public ConfigurationChain value(String value) {
    return new ConfigurationChain(configuration.or(Configuration.value(value)));
  }

}
