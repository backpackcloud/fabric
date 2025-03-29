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

import com.backpackcloud.UnbelievableException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static com.backpackcloud.reflection.predicates.ParameterPredicates.annotatedWith;
import static com.backpackcloud.reflection.predicates.ParameterPredicates.ofType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ContextTest {

  public void doSomething(String string) {
  }

  public void doSomething(Exception e) {
  }

  public void doSomething(String string, Exception e, @Deprecated int number) {
  }

  @Test
  public void test() throws NoSuchMethodException {
    Context context = new Context();
    context.when(ofType(String.class), "foo");
    context.when(ofType(Exception.class), () -> new UnbelievableException());
    context.when(annotatedWith(Deprecated.class), 10);

    Method method = ContextTest.class.getDeclaredMethod("doSomething", String.class);
    Object[] args = context.resolve(method);
    assertEquals(1, args.length);
    assertEquals("foo", args[0]);

    method = ContextTest.class.getDeclaredMethod("doSomething", Exception.class);
    args = context.resolve(method);
    assertEquals(1, args.length);
    assertInstanceOf(UnbelievableException.class, args[0]);

    method = ContextTest.class.getDeclaredMethod("doSomething", String.class, Exception.class, int.class);
    args = context.resolve(method);
    assertEquals(3, args.length);
    assertEquals("foo", args[0]);
    assertInstanceOf(UnbelievableException.class, args[1]);
    assertEquals(10, args[2]);
  }

}
