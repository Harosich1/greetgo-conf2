package kz.greetgo.conf2;

import java.util.Date;
import java.util.List;
import kz.greetgo.conf2.lines.ConfigLine;

public interface FileReader {

  List<ConfigLine> content();

  Date createdAt();

  Date lastModifiedAt();

}
