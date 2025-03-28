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

package com.backpackcloud.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/// A configuration based solely on a given value.
///
/// @author Ataxexe
public class RawValueConfiguration implements Configuration {

  private final String value;

  public RawValueConfiguration(String value) {
    this.value = value;
  }

  @Override
  public String get() {
    return this.value;
  }

  @Override
  public boolean isSet() {
    return true;
  }

  @Override
  public String read() {
    return value;
  }

  @Override
  public List<String> readLines() {
    Scanner scanner = new Scanner(read());
    List<String> lines = new ArrayList<>();
    while (scanner.hasNextLine()) {
      lines.add(scanner.nextLine());
    }
    return lines;
  }

}
