package kz.greetgo.conf2;

import java.util.List;
import java.util.Optional;
import kz.greetgo.conf2.lines.ConfigLine;

/**
 * Интерфейс реализации работы с файлом. Читает, создает, удаляет файл по указанному пути
 *
 */
public interface FileSystemAccess {

  /**
   * Читает содержимое файла по указанному пути. Если файла нет, то возвращается null
   *
   * @param path путь к файлу (вначале слэша нет)
   * @return содержимое файла или null, если нет файла
   */
  Optional<FileReader> readFile(String path);

  /**
   * Записывает содержимое lines в файл по указанному пути. Если файла нет, то создается новый.
   * Если lines являеется null файл по указаному пути файл удаляется
   *
   * @param path путь к файлу (вначале слэша нет)
   * @param lines содержимое для файла
   *
   */
  void writeFile(String path, List<ConfigLine> lines);

}
