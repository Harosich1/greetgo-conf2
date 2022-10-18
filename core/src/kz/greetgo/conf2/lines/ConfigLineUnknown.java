package kz.greetgo.conf2.lines;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ConfigLineUnknown implements ConfigLine {
  public final String lineText;

  @Override
  public String lineText() {
    return lineText;
  }
}
