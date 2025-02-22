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

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Context {

  private final List<Context.Entry> entries;

  public Context() {
    this.entries = new ArrayList<>();
  }

  public Context when(Predicate<? super Parameter> condition, Object object) {
    this.entries.add(new Entry(condition, parameter -> object));
    return this;
  }

  public Context when(Predicate<? super Parameter> condition, Supplier supplier) {
    this.entries.add(new Entry(condition, parameter -> supplier.get()));
    return this;
  }

  public Context when(Predicate<? super Parameter> condition, Function<Parameter, Object> function) {
    this.entries.add(new Entry(condition, function));
    return this;
  }

  public void orElse(Object object) {
    when(parameter -> true, object);
  }

  public void orElse(Supplier supplier) {
    when(parameter -> true, supplier);
  }

  public void orElse(Function<Parameter, Object> function) {
    when(parameter -> true, function);
  }

  public Optional<Object> resolve(Parameter parameter) {
    for (Context.Entry entry : entries) {
      if (entry.predicate.test(parameter)) {
        return Optional.ofNullable(entry.function.apply(parameter));
      }
    }
    return Optional.empty();
  }

  public Object[] resolve(Parameter[] parameters) {
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      args[i] = resolve(parameters[i]).orElse(null);
    }
    return args;
  }

  private record Entry(Predicate<? super Parameter> predicate, Function<Parameter, Object> function) {

  }

}
