package for_tests;

import java.util.Date;
import java.util.List;
import kz.greetgo.conf2.FileReader;
import kz.greetgo.conf2.lines.ConfigLine;

public class FileReaderForTests implements FileReader {

  public List<ConfigLine> content;

  public int contentReadCount = 0;

  @Override
  public List<ConfigLine> content() {
    contentReadCount++;
    return content;
  }

  public Date createdAt;

  @Override
  public Date createdAt() {
    if (createdAt == null) {
      throw new RuntimeException("K4KXl18T5K");
    }
    return createdAt;
  }

  public Date lastModifiedAt;

  public int lastModifiedAtCallCount = 0;

  @Override
  public Date lastModifiedAt() {
    if (lastModifiedAt == null) {
      throw new RuntimeException("JV80ut8lj3");
    }
    lastModifiedAtCallCount++;
    return lastModifiedAt;
  }
}
