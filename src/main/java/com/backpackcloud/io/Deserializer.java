package com.backpackcloud.io;

import java.io.File;
import java.io.InputStream;

public interface Deserializer {

  <E> E deserialize(String input, Class<E> type);
  <E> E deserialize(File file, Class<E> type);
  <E> E deserialize(InputStream input, Class<E> type);

}
