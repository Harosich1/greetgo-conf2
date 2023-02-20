package kz.greetgo.conf2;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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

  private final AtomicReference<List<ConfigLine>> cashedContent = new AtomicReference<>();

  private final AtomicLong lastDate = new AtomicLong();

  public List<ConfigLine> content() {

      long systemTime   = System.currentTimeMillis();
      long fileCallTime = currentTimeMs.getAsLong();
      if (cashedContent.get() == null || fileCallTime - systemTime >= delayBetweenReadMs.getAsLong()) {

        if (fs.readFile(path).isPresent()) {
          FileReader file = fs.readFile(path).get();

          if (lastDate.get() == 0) {
            lastDate.set(file.lastModifiedAt().getTime());
            cashedContent.set(file.content());
          } else if (lastDate.get() != file.lastModifiedAt().getTime()) {
            return file.content();
          }
        } else {
          fs.writeFile(path, defaultContentSupplier.get());
          cashedContent.set(defaultContentSupplier.get());
        }
      }

    return cashedContent.get();
  }
}
