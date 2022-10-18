package kz.greetgo.conf2.lines;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ConfigLineEmpty  implements ConfigLine{
  @Override
  public String lineText() {
    return "";
  }
}
