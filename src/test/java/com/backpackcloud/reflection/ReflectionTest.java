package com.backpackcloud.reflection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectionTest {

  @Test
  public void testListMethods() {
    List<ReflectedMethod> methods = Reflection.reflect(Object.class).methods().toList();

    assertEquals(12, methods.size());
  }

  @Test
  public void testFetchMethod() {
    assertTrue(Reflection.reflect(Object.class).method("clone").isPresent());
    assertTrue(Reflection.reflect(Object.class).method("toString").isPresent());
    assertTrue(Reflection.reflect(Object.class).method("wait").isPresent());
    assertTrue(Reflection.reflect(Object.class).method("wait", long.class).isPresent());
    assertTrue(Reflection.reflect(Object.class).method("wait", long.class, int.class).isPresent());
    assertTrue(Reflection.reflect(Object.class).method("notify").isPresent());
    assertTrue(Reflection.reflect(Object.class).method("equals", Object.class).isPresent());

    assertTrue(Reflection.reflect(Object.class).method("clone", String.class).isEmpty());
    assertTrue(Reflection.reflect(Object.class).method("equals", String.class).isEmpty());
  }

}
