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

  private final LongSupplier currentTimeMs;

  private List<ConfigLine> cashedContent;


  public List<ConfigLine> content() {

    if (fs.readFile(path).isPresent()) {
      FileReader file = fs.readFile(path).get();
      file.lastModifiedAt();

      long systemTime   = System.currentTimeMillis();
      long fileCallTime = currentTimeMs.getAsLong();

      if (cashedContent == null ||   fileCallTime - systemTime >= delayBetweenReadMs.getAsLong()) {
        cashedContent = file.content();
      }
    } else {
      fs.writeFile(path, defaultContentSupplier.get());
      cashedContent = defaultContentSupplier.get();
    }

    return cashedContent;
  }
}
