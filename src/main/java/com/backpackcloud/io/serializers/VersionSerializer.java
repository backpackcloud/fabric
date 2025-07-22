package com.backpackcloud.io.serializers;

import com.backpackcloud.versiontm.Version;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VersionSerializer extends JsonSerializer<Version> {

  @Override
  public Class<Version> handledType() {
    return Version.class;
  }

  @Override
  public void serialize(Version version, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(version.toString());
  }

}
