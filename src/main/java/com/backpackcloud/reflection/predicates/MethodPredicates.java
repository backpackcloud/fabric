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

package com.backpackcloud.reflection.predicates;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/// A class that holds a set of predicates for using with Method objects.
///
/// @author Ataxexe
public final class MethodPredicates {

  private MethodPredicates() {

  }

  /// Creates a predicate that tests if a method has the given name.
  ///
  /// @param name the name to check
  /// @return a new predicate
  public static Predicate<Method> ofName(String name) {
    return method -> method.getName().equals(name);
  }

  /// Creates a predicate that tests if a method is annotated with the given annotation.
  ///
  /// @param type the annotation to check
  /// @return a new predicate
  public static Predicate<Method> annotatedWith(Class<? extends Annotation> type) {
    return method -> method.isAnnotationPresent(type);
  }

}
