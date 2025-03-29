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

package com.backpackcloud.io;

import com.backpackcloud.UnbelievableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/// A simple and highly opinionated component based on the Jackson library to perform
///  serialization and deserialization.
///
/// @author Ataxexe
public class SerialBitter implements Serializer, Deserializer {

  private final ObjectMapper objectMapper;
  private final InjectableValues.Std values;

  /// Creates a new instance that will delegate the operations to the given object mapper.
  ///
  /// The mapper will be configured using the idiosyncrasies of this class... you've been warned.
  ///
  /// @param objectMapper the mapper to delegate the operations
  public SerialBitter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;

    this.values = new InjectableValues.Std();
    this.objectMapper.setInjectableValues(values);

    this.objectMapper.registerModules(new Jdk8Module(), new JavaTimeModule(), new ParameterNamesModule());
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    addDependency(SerialBitter.class, this);
  }

  /// @return the object mapper used by this instance
  public ObjectMapper mapper() {
    return objectMapper;
  }

  /// Adds a dependency using the given type.
  ///
  /// The dependency will be injected whenever the injection point is of the given type.
  ///
  /// @param type       the type of the injection point
  /// @param dependency the dependency to inject
  /// @return a reference to this serializer
  public <E> SerialBitter addDependency(Class<E> type, E dependency) {
    values.addValue(type, dependency);
    return this;
  }

  @Override
  public String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new UnbelievableException(e);
    }
  }

  /// Deserialize the given input into an object of the given class.
  ///
  /// @param input the input to deserialize
  /// @param type  the type of the result object
  /// @return the deserialized object.
  @Override
  public <E> E deserialize(String input, Class<E> type) {
    try {
      return objectMapper.readValue(input, type);
    } catch (IOException e) {
      throw new UnbelievableException(e);
    }
  }

  /// Deserialize the given file content into an object of the given class.
  ///
  /// @param file the file containing the input to deserialize
  /// @param type the type of the result object
  /// @return the deserialized object.
  @Override
  public <E> E deserialize(File file, Class<E> type) {
    try {
      return objectMapper.readValue(file, type);
    } catch (IOException e) {
      throw new UnbelievableException(e);
    }
  }

  /// Deserialize the given input into an object of the given class.
  ///
  /// @param input the input to deserialize
  /// @param type  the type of the result object
  /// @return the deserialized object.
  @Override
  public <E> E deserialize(InputStream input, Class<E> type) {
    try (input) {
      return objectMapper.readValue(input, type);
    } catch (IOException e) {
      throw new UnbelievableException(e);
    }
  }

  /// @return a new serializer for json contents
  public static SerialBitter JSON() {
    return new SerialBitter(new ObjectMapper());
  }

  /// @return a new serializer for yaml contents
  public static SerialBitter YAML() {
    return new SerialBitter(new ObjectMapper(new YAMLFactory()));
  }

  /// @return a new serializer for xml contents
  public static SerialBitter XML() {
    return new SerialBitter(new ObjectMapper(new XmlFactory()));
  }

}
