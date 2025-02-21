package com.backpackcloud.reflection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.backpackcloud.reflection.Reflection.reflect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectionTest {

  @Test
  public void testListMethods() {
    List<ReflectedMethod> methods = reflect(Object.class).methods().toList();

    assertEquals(12, methods.size());
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
    List<ReflectedField> fields = reflect(String.class).fields().toList();

    assertEquals(11, fields.size());
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
    assertEquals(1, reflect(Object.class).constructors().toList().size());
    assertEquals(19, reflect(String.class).constructors().toList().size());
  }

}
