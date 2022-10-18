package kz.greetgo.conf2.lines;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ConfigLineComment implements ConfigLine {
  public final String comment;

  @Override
  public String lineText() {
    return comment;
  }
}
