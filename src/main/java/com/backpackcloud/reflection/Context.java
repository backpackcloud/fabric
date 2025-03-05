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

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/// A class that aims to resolve {@link Parameter parameters} based on a definable context.
///
/// @author Ataxexe
public class Context {

  private final List<Context.Entry> entries;
  private final Function<Parameter, Object> defaultValue;

  /// Creates a new context that will default to the result of the given function.
  ///
  /// @param function the function to resolve the parameter by default
  public Context(Function<Parameter, Object> function) {
    this.entries = new ArrayList<>();
    this.defaultValue = function;
  }

  /// Creates a new context that will default to the result of the given supplier.
  ///
  /// @param supplier the supplier to resolve the parameter by default
  public Context(Supplier<Object> supplier) {
    this(parameter -> supplier.get());
  }

  /// Creates a new context that will default to the given object.
  ///
  /// @param defaultValue the object to resolve the parameter by default
  public Context(Object defaultValue) {
    this(parameter -> defaultValue);
  }

  /// Creates a new context that will default to {@code null}.
  public Context() {
    this(parameter -> null);
  }

  /// Defines a condition for this context to resolve a parameter to the given object.
  ///
  /// @param condition the condition to trigger this resolution
  /// @param object    the resolution object
  /// @return a reference to this context instance.
  public Context when(Predicate<? super Parameter> condition, Object object) {
    this.entries.add(new Entry(condition, parameter -> object));
    return this;
  }

  /// Defines a condition for this context to resolve a parameter to the given supplier.
  ///
  /// @param condition the condition to trigger this resolution
  /// @param supplier  the supplier to provide the resolution object
  /// @return a reference to this context instance.
  public Context when(Predicate<? super Parameter> condition, Supplier supplier) {
    this.entries.add(new Entry(condition, parameter -> supplier.get()));
    return this;
  }

  /// Defines a condition for this context to resolve a parameter to the given function.
  ///
  /// @param condition the condition to trigger this resolution
  /// @param function  the function to provide the resolution object
  /// @return a reference to this context instance.
  public Context when(Predicate<? super Parameter> condition, Function<Parameter, Object> function) {
    this.entries.add(new Entry(condition, function));
    return this;
  }

  /// Tries to resolve the argument that fits the given parameter.
  ///
  /// The conditions are checked in the order they were added to the context.
  /// Only the first match is used.
  ///
  /// @param parameter the parameter to resolve the argument
  /// @return the found argument
  public Optional<Object> resolve(Parameter parameter) {
    for (Context.Entry entry : entries) {
      if (entry.predicate.test(parameter)) {
        return Optional.ofNullable(entry.function.apply(parameter));
      }
    }
    return Optional.ofNullable(defaultValue.apply(parameter));
  }

  /// Tries to resolve the arguments that matches each parameter.
  ///
  /// The conditions are checked in the order they were added to the context.
  /// Only the first match is used.
  ///
  /// @param parameters the parameters to resolve the argument
  /// @return the found arguments
  public Object[] resolve(Parameter[] parameters) {
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      args[i] = resolve(parameters[i]).orElse(null);
    }
    return args;
  }

  /// Short for {@code resolve(executable.getParameters())}
  ///
  /// @see #resolve(Parameter[])
  public Object[] resolve(Executable executable) {
    return resolve(executable.getParameters());
  }

  private record Entry(Predicate<? super Parameter> predicate,
                       Function<Parameter, Object> function) {
  }

}
