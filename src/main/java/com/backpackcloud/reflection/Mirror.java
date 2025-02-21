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

public class Mirror {

  private final Class targetType;
  private final List<Class> targetHierarchy;

  public Mirror(Class targetType) {
    this.targetType = targetType;
    this.targetHierarchy = new ArrayList<>();
    for (Class c = targetType; c != null; c = c.getSuperclass()) {
      this.targetHierarchy.add(c);
    }
  }

  public List<Field> fields() {
    List<Field> result = new ArrayList<>();
    for (Class type : targetHierarchy) {
      result.addAll(List.of(type.getDeclaredFields()));
    }
    return result;
  }

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

  public List<Method> methods() {
    List<Method> result = new ArrayList<>();
    for (Class type : targetHierarchy) {
      result.addAll(List.of(type.getDeclaredMethods()));
    }
    return result;
  }

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

  public List<Constructor> constructors() {
    return List.of(targetType.getDeclaredConstructors());
  }

  public Optional<Constructor> constructor(Class<?>... parameterTypes) {
    if (parameterTypes != null) {
      try {
        return Optional.of(targetType.getDeclaredConstructor(parameterTypes));
      } catch (NoSuchMethodException e) {
        return Optional.empty();
      }
    }
    return Stream.of(targetType.getDeclaredConstructors())
      .findFirst();
  }

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
