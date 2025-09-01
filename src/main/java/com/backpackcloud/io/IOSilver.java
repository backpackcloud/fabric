package com.backpackcloud.io;

import java.util.function.Supplier;

/// A triad that dominated the western serial bitter movies. Able to tackle JSON, YAML
/// and XML formats.
///
/// @author Ataxexe
public class IOSilver {

  private final Supplier<SerialBitter> jsonSupplier;
  private final Supplier<SerialBitter> xmlSupplier;
  private final Supplier<SerialBitter> yamlSupplier;

  private SerialBitter json;
  private SerialBitter xml;
  private SerialBitter yaml;

  public IOSilver() {
    this(
      SerialBitter::JSON,
      SerialBitter::XML,
      SerialBitter::YAML
    );
  }

  public IOSilver(Supplier<SerialBitter> jsonSupplier,
                  Supplier<SerialBitter> xmlSupplier,
                  Supplier<SerialBitter> yamlSupplier) {
    this.jsonSupplier = jsonSupplier;
    this.xmlSupplier = xmlSupplier;
    this.yamlSupplier = yamlSupplier;
  }

  public SerialBitter json() {
    if (json == null) {
      this.json = jsonSupplier.get();
      this.json.addDependency(IOSilver.class, this);
    }
    return json;
  }

  public SerialBitter xml() {
    if (xml == null) {
      this.xml = xmlSupplier.get();
      this.xml.addDependency(IOSilver.class, this);
    }
    return xml;
  }

  public SerialBitter yaml() {
    if (yaml == null) {
      this.yaml = yamlSupplier.get();
      this.yaml.addDependency(IOSilver.class, this);
    }
    return yaml;
  }

}
