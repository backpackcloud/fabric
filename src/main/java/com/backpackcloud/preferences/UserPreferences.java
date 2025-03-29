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

import com.backpackcloud.UnbelievableException;
import com.backpackcloud.reflection.Mirror;
import com.backpackcloud.reflection.predicates.FieldPredicates;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/// Defines a container that manages user preferences across the application.
///
/// @author Marcelo "Ataxexe“ Guimarães
public class UserPreferences {

  private final Map<String, Preference> preferencesMap = new ConcurrentHashMap<>();

  public <E> Preference<E> register(PreferenceSpec<E> spec) {
    if (!preferencesMap.containsKey(spec.id())) {
      Preference<?> preference = new Preference<>(spec);
      this.preferencesMap.put(spec.id(), preference);
    }
    return preferencesMap.get(spec);
  }

  /// Registers all the
  public void register(PreferenceSpec... specs) {
    for (PreferenceSpec spec : specs) {
      register(spec);
    }
  }

  public void register(Class container) {
    Mirror.reflect(container).fields().stream()
      .filter(FieldPredicates.ofType(PreferenceSpec.class))
      .filter(field -> Modifier.isStatic(field.getModifiers()))
      .map(field -> {
        try {
          return (PreferenceSpec) field.get(container);
        } catch (IllegalAccessException e) {
          throw new UnbelievableException(e);
        }
      })
      .forEach(this::register);
  }

  /**
   * Finds the preference identified by the given id.
   * <p>
   * Returns an empty value if the preference is not found.
   *
   * @param id the preference's identifier
   * @return the found preference
   * @see PreferenceSpec#id()
   */
  public <E> Optional<Preference<E>> find(String id) {
    return Optional.ofNullable(preferencesMap.get(id));
  }

  /**
   * Returns the preference specified by the given spec.
   * <p>
   * This method is guaranteed to return a preference object,
   * even if there wasn't any stored at the moment.
   *
   * @param spec the preference's specification
   * @return the managed preference
   */
  public <E> Preference<E> get(PreferenceSpec<E> spec) {
    if (!preferencesMap.containsKey(spec.id())) {
      preferencesMap.put(spec.id(), new Preference<>(spec));
    }
    return preferencesMap.get(spec.id());
  }

  /**
   * @return all the preferences currently managed
   */
  public List<Preference<?>> list() {
    return new ArrayList(preferencesMap.values());
  }

  public <E> UserPreferences watch(PreferenceSpec<E> spec, Consumer<E> action) {
    Preference preference = get(spec);
    preference.listen(action);
    return this;
  }

  public <E> Supplier<E> supplier(PreferenceSpec<E> spec) {
    return () -> (E) get(spec).value();
  }

  /**
   * Checks if the given `Boolean` preference is enabled.
   *
   * @param spec the preference spec
   * @return {@code true} if the related preference holds a {@code true} value.
   */
  public boolean isEnabled(PreferenceSpec<Boolean> spec) {
    return supplier(spec).get();
  }

  /**
   * Checks if the given `Boolean` preference is disabled.
   *
   * @param spec the preference spec
   * @return {@code true} if the related preference holds a {@code false} value.
   */
  public boolean isDisabled(PreferenceSpec<Boolean> spec) {
    return !isEnabled(spec);
  }

}
