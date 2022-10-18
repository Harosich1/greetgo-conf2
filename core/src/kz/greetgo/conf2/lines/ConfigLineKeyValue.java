package kz.greetgo.conf2.lines;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ConfigLineKeyValue implements ConfigLine {

  public final String  key;
  public final String  value;
  public final boolean isCommented;

  @Override
  public String lineText() {
    return (isCommented ? "#" : "") + key + (value == null ? "" : "=" + value);
  }
}
