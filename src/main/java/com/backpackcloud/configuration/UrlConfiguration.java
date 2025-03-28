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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/// A configuration based on the contents of a given url.
///
/// @author Ataxexe
public class UrlConfiguration implements Configuration {

  private final URL url;
  private String content;

  public UrlConfiguration(String url) {
    try {
      this.url = new URL(url);
    } catch (MalformedURLException e) {
      throw new UnbelievableException(e);
    }
  }

  @Override
  public boolean isSet() {
    return true;
  }

  @Override
  public String get() {
    return url.toExternalForm();
  }

  @Override
  public String read() {
    load();
    return content;
  }

  @Override
  public List<String> readLines() {
    load();
    return content.lines().collect(Collectors.toList());
  }

  private void load() {
    if (content != null) {
      try {
        content = url.getContent().toString();
      } catch (IOException e) {
        throw new UnbelievableException(e);
      }
    }
  }

}
