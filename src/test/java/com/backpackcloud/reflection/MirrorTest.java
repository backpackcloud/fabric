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

import org.junit.jupiter.api.Test;

import static com.backpackcloud.reflection.Mirror.reflect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MirrorTest {

  @Test
  public void testListMethods() {
    assertEquals(12, reflect(Object.class).methods().size());
  }

  @Test
  public void testFetchMethod() {
    assertTrue(reflect(String.class).method("clone").isPresent());
    assertTrue(reflect(String.class).method("toString").isPresent());
    assertTrue(reflect(String.class).method("wait").isPresent());
    assertTrue(reflect(String.class).method("wait", long.class).isPresent());
    assertTrue(reflect(String.class).method("wait", long.class, int.class).isPresent());
    assertTrue(reflect(String.class).method("notify").isPresent());
    assertTrue(reflect(String.class).method("equals", Object.class).isPresent());
    assertTrue(reflect(String.class).method("coder").isPresent());
    assertTrue(reflect(String.class).method("startsWith", String.class).isPresent());

    assertTrue(reflect(String.class).method("clone", String.class).isEmpty());
    assertTrue(reflect(String.class).method("equals", String.class).isEmpty());
  }

  @Test
  public void testListFields() {
    assertEquals(11, reflect(String.class).fields().size());
  }

  @Test
  public void testFetchField() {
    assertTrue(reflect(String.class).field("value").isPresent());
    assertTrue(reflect(String.class).field("coder").isPresent());
    assertTrue(reflect(String.class).field("hash").isPresent());
    assertTrue(reflect(String.class).field("impossible_to_exist").isEmpty());
  }

  @Test
  public void testListConstructors() {
    assertEquals(1, reflect(Object.class).constructors().size());
    assertEquals(15, reflect(String.class).constructors().size());
  }

}
