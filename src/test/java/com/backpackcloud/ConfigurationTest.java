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

package com.backpackcloud;

import com.backpackcloud.configuration.Configuration;
import com.backpackcloud.configuration.EnvironmentVariableConfiguration;
import com.backpackcloud.configuration.FileConfiguration;
import com.backpackcloud.configuration.RawValueConfiguration;
import com.backpackcloud.configuration.ResourceConfiguration;
import org.junit.jupiter.api.Test;

import static com.backpackcloud.configuration.Configuration.configuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationTest {

  @Test
  public void testRawValue() {
    Configuration value = Configuration.value("foo");
    assertInstanceOf(RawValueConfiguration.class, value);
    assertTrue(value.isSet());
    assertEquals("foo", value.get());

    value = Configuration.value("bar");
    assertInstanceOf(RawValueConfiguration.class, value);
    assertTrue(value.isSet());
    assertEquals("bar", value.get());

    value = configuration()
      .env("NOT_SET")
      .property("not.set")
      .file("NONE")
      .resource("META-INF/NOTHING")
      .value("foo");
    assertTrue(value.isSet());
    assertEquals("foo", value.get());
  }

  @Test
  public void testEnvironmentVariableValue() {
    Configuration value = Configuration.env("JAVA_HOME");
    assertInstanceOf(EnvironmentVariableConfiguration.class, value);
    assertTrue(value.isSet());
    assertEquals(System.getenv("JAVA_HOME"), value.get());

    value = configuration()
      .env("JAVA_HOME")
      .property("property.test")
      .file("LICENSE")
      .resource("com.backpackcloud/LICENSE")
      .value("foo");
    assertTrue(value.isSet());
    assertEquals(System.getenv("JAVA_HOME"), value.get());
  }

  @Test
  public void testSystemPropertyValue() {
    Configuration value = Configuration.property("property.test");
    assertFalse(value.isSet());

    System.setProperty("property.test", "bar");

    assertTrue(value.isSet());
    assertEquals("bar", value.get());
  }

  @Test
  public void testFileValue() {
    Configuration value = Configuration.file("LICENSE");
    assertInstanceOf(FileConfiguration.class, value);
    assertTrue(value.isSet());
    assertTrue(value.get().contains("The MIT License (MIT)"));

    assertFalse(Configuration.file("nothing_here.txt").isSet());

    value = configuration()
      .env("NOT_SET")
      .property("not.set")
      .file("LICENSE")
      .resource("com.backpackcloud/LICENSE")
      .value("foo");
    assertTrue(value.isSet());
    assertTrue(value.get().contains("The MIT License (MIT)"));
  }

  @Test
  public void testResourceValue() {
    Configuration value = Configuration.resource("com.backpackcloud/LICENSE");
    assertInstanceOf(ResourceConfiguration.class, value);
    assertTrue(value.isSet());
    assertTrue(value.get().contains("The MIT License (MIT)"));

    value = configuration()
      .env("NOT_SET")
      .property("not.set")
      .file("NONE")
      .resource("com.backpackcloud/LICENSE")
      .value("foo");
    assertTrue(value.isSet());
    assertTrue(value.get().contains("The MIT License (MIT)"));
  }

  @Test
  public void testIntConversion() {
    Configuration value = Configuration.value("10");
    assertTrue(value.isSet());
    assertEquals("10", value.get());
    assertEquals(10, value.asInt());
  }

  @Test
  public void testLongConversion() {
    Configuration value = Configuration.value("100000000000000");
    assertTrue(value.isSet());
    assertEquals("100000000000000", value.get());
    assertEquals(100000000000000L, value.asLong());
  }

  @Test
  public void testBooleanConversion() {
    Configuration value = Configuration.value("true");
    assertTrue(value.isSet());
    assertEquals("true", value.get());
    assertTrue(value.asBoolean());
  }

  @Test
  public void testNotSuppliedConfiguration() {
    Configuration value = Configuration.NOT_SUPPLIED;
    assertFalse(value.isSet());
    assertEquals("", value.get());
    assertEquals(0, value.asInt());
    assertEquals(0L, value.asLong());
    assertFalse(value.asBoolean());
    assertEquals("", value.read());
    assertTrue(value.readLines().isEmpty());
  }

}
