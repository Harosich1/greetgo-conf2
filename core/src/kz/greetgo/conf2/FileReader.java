package kz.greetgo.conf2;

import java.util.Date;
import java.util.List;
import kz.greetgo.conf2.lines.ConfigLine;

/**
 * Реализация прочтения файла
 *
 */
public interface FileReader {

  /**
   * @return Список строчек которые содержатся в файле
   */
  List<ConfigLine> content();

  /**
   * @return Дату создания файла
   */

  Date createdAt();

  /**
   * @return Дату изменения файла
   */

  Date lastModifiedAt();

}
