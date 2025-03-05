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

package com.backpackcloud.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/// A class that acts as a helper for basic reflection operations.
///
/// Use this class if you need to go through all the hierarchy of a
/// target in search for elements.
///
/// @author Ataxexe
public class Mirror {

  /// The target of this mirror instance
  private final Class targetType;
  /// The computed class hierarchy of the target
  private final List<Class> targetHierarchy;

  /// Creates a new mirror targeting the given class
  ///
  /// @param targetType the target class
  public Mirror(Class targetType) {
    this.targetType = targetType;
    this.targetHierarchy = new ArrayList<>();
    for (Class c = targetType; c != null; c = c.getSuperclass()) {
      this.targetHierarchy.add(c);
    }
  }

  /// Reflects the fields of the target class and its superclasses.
  ///
  /// @return a list of every field found in the target hierarchy.
  /// @see Class#getDeclaredFields()
  public List<Field> fields() {
    List<Field> result = new ArrayList<>();
    for (Class type : targetHierarchy) {
      result.addAll(List.of(type.getDeclaredFields()));
    }
    return result;
  }

  /// Reflects the field declared with the given name. If there are
  /// multiple fields with the same name down in the target hierarchy,
  /// returns the one found in the furthest class from Object.
  ///
  /// @param name the name of the field
  /// @return the field that is declared with the given name.
  /// @see Class#getDeclaredField(String)
  public Optional<Field> field(String name) {
    return targetHierarchy
      .stream()
      .map(type -> {
        try {
          return type.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
          return null;
        }
      })
      .filter(Objects::nonNull)
      .findFirst();
  }

  /// Reflects the methods of the target class and its superclasses.
  ///
  /// @return a list of every method found in the target hierarchy.
  /// @see Class#getDeclaredMethods()
  public List<Method> methods() {
    List<Method> result = new ArrayList<>();
    for (Class type : targetHierarchy) {
      result.addAll(List.of(type.getDeclaredMethods()));
    }
    return result;
  }

  /// Reflects the method declared with the given name and parameter types.
  /// If there are multiple methods with the same signature in the target
  /// hierarchy, returns the one found in the furthest class from Object.
  ///
  /// @param name           the name of the method
  /// @param parameterTypes the parameter types
  /// @return the field that is declared with the given name.
  /// @see Class#getDeclaredMethod(String, Class[])
  public Optional<Method> method(String name, Class<?>... parameterTypes) {
    if (parameterTypes.length > 0) {
      return targetHierarchy
        .stream()
        .map(type -> {
          try {
            return type.getDeclaredMethod(name, parameterTypes);
          } catch (NoSuchMethodException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .findFirst();
    }

    return targetHierarchy
      .stream()
      .flatMap(type -> Stream.of(type.getDeclaredMethods()))
      .filter(method -> method.getName().equals(name))
      .findFirst();
  }

  /// Reflects the constructors of the target.
  ///
  /// @return the constructors found in the target class.
  /// @see Class#getDeclaredConstructors()
  public List<Constructor> constructors() {
    return List.of(targetType.getDeclaredConstructors());
  }

  /// Reflects the constructor that matches the given signature.
  ///
  /// @param parameterTypes the parameter types declared in the constructor
  /// @return the constructor that matches the given signature.
  /// @see Class#getDeclaredConstructor(Class[])
  public Optional<Constructor> constructor(Class<?>... parameterTypes) {
    try {
      return Optional.of(targetType.getDeclaredConstructor(parameterTypes));
    } catch (NoSuchMethodException e) {
      return Optional.empty();
    }
  }

  /// Creates a new Mirror targeting the type of the given object.
  ///
  /// @return a Mirror instance targeting the object's type.
  /// @see Mirror#Mirror(Class)
  public static Mirror reflect(Object target) {
    Class type;
    if (target instanceof Annotation) {
      type = ((Annotation) target).annotationType();
    } else {
      type = target instanceof Class ? ((Class<?>) target) : target.getClass();
    }
    return new Mirror(type);
  }

}
