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

package com.backpackcloud.preferences;

import com.backpackcloud.text.InputValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/// Defines a preference for the application.
///
/// A preference is essentially a configuration that can be changed by
/// a user in runtime.
///
/// @param <E>
/// @author Marcelo "Ataxexe" Guimarães
public class Preference<E> {

  private final PreferenceSpec<E> spec;
  private final List<Consumer<E>> listeners;
  private E currentValue;
  private String currentInputValue;

  public Preference(PreferenceSpec<E> spec) {
    this.spec = spec;
    this.listeners = new ArrayList<>();
    reset();
  }

  /// @return the spec for this preference.
  public PreferenceSpec<E> spec() {
    return this.spec;
  }

  /// @return the current value this preference is holding.
  public E value() {
    return this.currentValue;
  }

  /// @return the input value which generated the current [#value()].
  public InputValue inputValue() {
    return InputValue.of(this.currentInputValue);
  }

  /// Changes the value by supplying an input String.
  ///
  /// @param input the input string for producing the value.
  /// @see PreferenceType#convert(String)
  public void set(String input) {
    this.currentValue = spec().type().convert(input);
    this.currentInputValue = input;
    this.listeners.forEach(listener -> listener.accept(this.currentValue));
  }

  /// Reverts the value back to its
  /// [default][PreferenceSpec#defaultValue()] one.
  public void reset() {
    set(spec().defaultValue());
  }

  /// Listens to any value change for this preference.
  ///
  /// In case the value gets changed or cleared, the given
  /// listener will be notified immediately in the same
  /// Thread.
  ///
  /// The listener will be notified about the current value
  /// of the property as soon as it's registered, so there is
  /// no need for getting the current value prior to listen
  /// to changes.
  ///
  /// @param listener the listener to notify on value change
  public void listen(Consumer<E> listener) {
    listener.accept(this.currentValue);
    this.listeners.add(listener);
  }

}
