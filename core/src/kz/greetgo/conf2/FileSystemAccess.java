package kz.greetgo.conf2;

import java.util.List;
import java.util.Optional;
import kz.greetgo.conf2.lines.ConfigLine;

/**
 * asd
 */
public interface FileSystemAccess {

  /**
   * Читает содержимое файла по указанному пути. Если файла нет, то возвращается null
   *
   * @param path путь к файлу (вначале слэша нет)
   * @return содержимое файла или null, если нет файла
   */
  Optional<FileReader> readFile(String path);

  void writeFile(String path, List<ConfigLine> lines);

}
