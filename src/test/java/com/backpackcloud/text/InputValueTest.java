package com.backpackcloud.text;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InputValueTest {

  @Test
  void get() {
    InputValue value = InputValue.of("foo");

    assertEquals("foo", value.get());
  }

  @Test
  void asText() {
    InputValue value = InputValue.of("foo");
    Optional<String> text = value.asText();

    assertTrue(text.isPresent());
    assertEquals("foo", text.get());

    value = InputValue.of("");
    text = value.asText();

    assertTrue(text.isEmpty());

    value = InputValue.of((String) null);
    text = value.asText();

    assertTrue(text.isEmpty());
  }

  @Test
  void map() {
    InputValue value = InputValue.of("foo");
    Function<String, String> function = Mockito.mock(Function.class);
    when(function.apply("foo")).thenReturn("bar");

    Optional<String> result = value.map(function);

    assertTrue(result.isPresent());
    assertEquals("bar", result.get());

    verify(function, times(1)).apply("foo");
  }

  @Test
  void asInteger() {
    InputValue value = InputValue.of("1");
    Optional<Integer> integer = value.asInteger();

    assertTrue(integer.isPresent());
    assertEquals(1, integer.get());

    value = InputValue.of("a");
    integer = value.asInteger();

    assertTrue(integer.isEmpty());

    value = InputValue.EMPTY;
    integer = value.asInteger();

    assertTrue(integer.isEmpty());
  }

  @Test
  void asLong() {
    InputValue value = InputValue.of("1");
    Optional<Long> number = value.asLong();

    assertTrue(number.isPresent());
    assertEquals(1, number.get());

    value = InputValue.of("a");
    number = value.asLong();

    assertTrue(number.isEmpty());

    value = InputValue.EMPTY;
    number = value.asLong();

    assertTrue(number.isEmpty());
  }

  @Test
  void asDouble() {
    InputValue value = InputValue.of("1.2");
    Optional<Double> number = value.asDouble();

    assertTrue(number.isPresent());
    assertEquals(1.2, number.get());

    value = InputValue.of("a");
    number = value.asDouble();

    assertTrue(number.isEmpty());

    value = InputValue.EMPTY;
    number = value.asDouble();

    assertTrue(number.isEmpty());
  }

  @Test
  void asBoolean() {
    InputValue value = InputValue.of("true");
    Optional<Boolean> bool = value.asBoolean();

    assertTrue(bool.isPresent());
    assertTrue(bool.get());

    value = InputValue.of("false");
    bool = value.asBoolean();

    assertTrue(bool.isPresent());
    assertFalse(bool.get());
  }

  @Test
  void asTemporal() {
    InputValue value = InputValue.of("2025-03-11");
    Optional<LocalDate> date = value.asTemporal("yyyy-MM-dd", LocalDate::from);

    assertTrue(date.isPresent());

    LocalDate localDate = date.get();

    assertEquals(2025, localDate.getYear());
    assertEquals(Month.MARCH, localDate.getMonth());
    assertEquals(11, localDate.getDayOfMonth());

    value = InputValue.of("not-a-date");
    date = value.asTemporal("yyyy-MM-dd", LocalDate::from);

    assertFalse(date.isPresent());
  }

  @Test
  void asEnum() {
    InputValue value = InputValue.of("march");
    Optional<Month> month = value.asEnum(Month.class);

    assertTrue(month.isPresent());
    assertEquals(Month.MARCH, month.get());

    value = InputValue.of("março");
    month = value.asEnum(Month.class);

    assertFalse(month.isPresent());
  }

  @Test
  void split() {
    InputValue value = InputValue.of("a, b, c, d, e");
    List<InputValue> values = value.split().toList();

    assertEquals(5, values.size());
    assertArrayEquals(
      new String[]{"a", "b", "c", "d", "e"},
      values.stream().map(InputValue::get).toArray(String[]::new)
    );
  }

  @Test
  void of() {
    Supplier<String> supplier = Mockito.mock(Supplier.class);
    when(supplier.get()).thenReturn("foo");

    InputValue value = InputValue.of(supplier);
    assertEquals("foo", value.get());

    verify(supplier, times(1)).get();
  }

}