package com.backpackcloud.reflection;

import com.backpackcloud.UnbelievableException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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
    context.whenOfType(String.class).use("foo");
    context.whenOfType(Exception.class).use(() -> new UnbelievableException());
    context.whenAnnotatedWith(Deprecated.class).use(10);

    Method method = ContextTest.class.getDeclaredMethod("doSomething", String.class);
    Object[] args = context.resolve(method.getParameters());
    assertEquals(1, args.length);
    assertEquals("foo", args[0]);

    method = ContextTest.class.getDeclaredMethod("doSomething", Exception.class);
    args = context.resolve(method.getParameters());
    assertEquals(1, args.length);
    assertInstanceOf(UnbelievableException.class, args[0]);

    method = ContextTest.class.getDeclaredMethod("doSomething", String.class, Exception.class, int.class);
    args = context.resolve(method.getParameters());
    assertEquals(3, args.length);
    assertEquals("foo", args[0]);
    assertInstanceOf(UnbelievableException.class, args[1]);
    assertEquals(10, args[2]);
  }

}
