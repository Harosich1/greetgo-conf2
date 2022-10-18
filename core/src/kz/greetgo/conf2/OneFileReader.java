package kz.greetgo.conf2;

import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import kz.greetgo.conf2.lines.ConfigLine;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OneFileReader {

  private final String path;

  private final FileSystemAccess fs;

  private final Supplier<List<ConfigLine>> defaultContentSupplier;

  private final LongSupplier delayBetweenReadMs;

  public List<ConfigLine> content() {
    throw new RuntimeException("lNdqVG1KHe :: Not impl yet");
  }
}
