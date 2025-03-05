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

import com.backpackcloud.UnbelievableException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceConfiguration implements Configuration {

  private final ClassLoader classLoader;
  private final String resourcePath;

  public ResourceConfiguration(ClassLoader classLoader, String resourcePath) {
    this.classLoader = classLoader;
    this.resourcePath = resourcePath;
  }

  public ResourceConfiguration(String resourcePath) {
    this(Thread.currentThread().getContextClassLoader(), resourcePath);
  }

  @Override
  public boolean isSet() {
    return classLoader.getResource(resourcePath) != null;
  }

  @Override
  public String get() {
    InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
    if (inputStream != null) {
      try (inputStream) {
        return new String(inputStream.readAllBytes());
      } catch (IOException e) {
        throw new UnbelievableException(e);
      }
    }
    return null;
  }

  @Override
  public String read() {
    return get();
  }

  @Override
  public List<String> readLines() {
    InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
    try (inputStream) {
      Scanner scanner = new Scanner(inputStream);
      List<String> lines = new ArrayList<>();
      while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine());
      }
      return lines;
    } catch (IOException e) {
      throw new UnbelievableException(e);
    }
  }

}
