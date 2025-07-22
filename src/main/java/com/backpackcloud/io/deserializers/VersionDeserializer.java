package com.backpackcloud.io.deserializers;

import com.backpackcloud.versiontm.Version;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class VersionDeserializer extends JsonDeserializer<Version> {

  @Override
  public Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
    JsonNode jsonNode = ctxt.readTree(p);
    return Version.of(jsonNode.asText());
  }

}
