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

import java.io.File;
import java.io.InputStream;

/// Interface that defines a component capable of deserializing contents.
///
/// @author Ataxexe
public interface Deserializer {

  /// Deserializes the contents of the given String into an object of the given `type`.
  ///
  /// @param content the content to deserialize
  /// @param type    the type of the object
  /// @return an object of the given `type`
  <E> E deserialize(String content, Class<E> type);

  /// Deserializes the contents in the given file into an object of the given `type`.
  ///
  /// @param file the file containing the content to deserialize
  /// @param type the type of the object
  /// @return an object of the given `type`
  <E> E deserialize(File file, Class<E> type);

  /// Deserializes the contents of the given input stream into an object of the given `type`.
  ///
  /// @param input the input stream containing the content to deserialize
  /// @param type  the type of the object
  /// @return an object of the given `type`
  <E> E deserialize(InputStream input, Class<E> type);

}
