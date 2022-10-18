package for_tests;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import kz.greetgo.conf2.FileReader;
import kz.greetgo.conf2.FileSystemAccess;
import kz.greetgo.conf2.lines.ConfigLine;

public class FileSystemAccessForTests implements FileSystemAccess {

  public final Map<String, FileReaderForTests> allFiles = new HashMap<>();

  public Supplier<Date> nowSupplier;

  @Override
  public Optional<FileReader> readFile(String path) {
    return Optional.ofNullable(allFiles.get(path));
  }

  @Override
  public void writeFile(String path, List<ConfigLine> lines) {
    FileReaderForTests file = allFiles.get(path);

    if (file == null) {
      if (lines == null) {
        return;
      }

      FileReaderForTests f = new FileReaderForTests();
      f.lastModifiedAt = nowSupplier.get();
      f.createdAt      = f.lastModifiedAt;
      f.content        = lines;
      allFiles.put(path, f);
      return;
    }

    if (lines == null) {
      allFiles.remove(path);
      return;
    }

    file.content        = lines;
    file.lastModifiedAt = nowSupplier.get();
  }
}
