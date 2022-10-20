package kz.greetgo.conf2;

import java.util.GregorianCalendar;
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

  private List<ConfigLine> cashedContent;

  public List<ConfigLine> content() {

    long lastUpdateMs;

    FileReader file = fs.readFile(path).orElseThrow();

    lastUpdateMs  = file.lastModifiedAt().getTime();
    if (new GregorianCalendar().getTime().getTime() - lastUpdateMs >= delayBetweenReadMs.getAsLong()) {
      return file.content();
    }
    else {
      cashedContent = file.content();
    }

    return cashedContent;
  }
}
