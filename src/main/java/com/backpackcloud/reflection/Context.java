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
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Context {

  private final List<Context.Entry> entries;
  private Function<Parameter, Object> defaultFunction = parameter -> null;

  public Context() {
    this.entries = new ArrayList<>();
  }

  public Mapper when(Predicate<? super Parameter> condition) {
    return function -> {
      entries.add(new Context.Entry(function, condition));
      return Context.this;
    };
  }

  public Mapper orElse() {
    return function -> {
      defaultFunction = function;

      return Context.this;
    };
  }

  public Mapper when(String name) {
    return when(parameter -> parameter.getName().equals(name));
  }

  public Mapper when(Class<?> type) {
    return when(parameter -> type.isAssignableFrom(parameter.getType()));
  }

  public Mapper whenAnnotatedWith(Class<? extends Annotation> annotationType) {
    return when(parameter -> parameter.isAnnotationPresent(annotationType));
  }

  public Optional<Object> resolve(Parameter parameter) {
    for (Context.Entry entry : entries) {
      if (entry.predicate.test(parameter)) {
        return Optional.ofNullable(entry.function.apply(parameter));
      }
    }
    return Optional.ofNullable(defaultFunction.apply(parameter));
  }

  public Object[] resolve(Parameter[] parameters) {
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      args[i] = resolve(parameters[i]).orElse(null);
    }
    return args;
  }

  private record Entry(Function<Parameter, Object> function, Predicate<? super Parameter> predicate) {

  }

  public interface Mapper {

    default Context use(Object object) {
      return use((parameter) -> object);
    }

    default Context use(Supplier supplier) {
      return use(parameter -> supplier.get());
    }

    Context use(Function<Parameter, Object> function);

  }

}
