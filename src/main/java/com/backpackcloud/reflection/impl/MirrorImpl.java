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

package com.backpackcloud.reflection.impl;

import com.backpackcloud.reflection.ClassIterator;
import com.backpackcloud.reflection.Mirror;
import com.backpackcloud.reflection.ReflectedConstructor;
import com.backpackcloud.reflection.ReflectedField;
import com.backpackcloud.reflection.ReflectedMethod;
import com.backpackcloud.reflection.Reflection;
import com.backpackcloud.reflection.ReflectionPredicates;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MirrorImpl implements Mirror {

  private final Object target;

  public MirrorImpl(Object target) {
    this.target = target;
  }

  @Override
  public Stream<ReflectedField> fields() {
    return new ClassIterator(target)
      .stream()
      .flatMap(type -> Stream.of(type.getDeclaredFields()))
      .map(field -> new TruggerReflectedField(field, target));
  }

  @Override
  public Optional<ReflectedField> field(String name) {
    return new ClassIterator(target)
      .stream()
      .map(type -> {
        try {
          return type.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
          return null;
        }
      })
      .filter(Objects::nonNull)
      .findFirst()
      .map(field -> new TruggerReflectedField(field, target));
  }

  @Override
  public Stream<ReflectedMethod> methods() {
    return Stream.empty();
  }

  @Override
  public Optional<ReflectedMethod> method(String name, Class<?>... parameterTypes) {
    if (parameterTypes.length == 0) {
      return new ClassIterator(target)
        .stream()
        .map(type -> {
          try {
            return type.getDeclaredMethod(name, parameterTypes);
          } catch (NoSuchMethodException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .findFirst()
        .map(method -> new TruggerReflectedMethod(method, target));
    }

    return new ClassIterator(target)
      .stream()
      .flatMap(type -> Stream.of(type.getDeclaredMethods()))
      .filter(ReflectionPredicates.ofName(name))
      .findFirst()
      .map(method -> new TruggerReflectedMethod(method, target));
  }

  @Override
  public Stream<ReflectedConstructor> constructors() {
    return Stream.of(Reflection.typeOf(target).getDeclaredConstructors())
      .map(TruggerReflectedConstructor::new);
  }

  @Override
  public Optional<ReflectedConstructor> constructor(Class<?>... parameterTypes) {
    if (parameterTypes != null) {
      try {
        return Optional.of(Reflection.typeOf(target).getDeclaredConstructor(parameterTypes))
          .map(TruggerReflectedConstructor::new);
      } catch (NoSuchMethodException e) {
        return Optional.empty();
      }
    }
    return Stream.of(Reflection.typeOf(target).getDeclaredConstructors())
      .findFirst()
      .map(TruggerReflectedConstructor::new);
  }

}
