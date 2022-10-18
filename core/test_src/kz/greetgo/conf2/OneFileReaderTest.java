package kz.greetgo.conf2;

import for_tests.FileSystemAccessForTests;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import kz.greetgo.conf2.lines.ConfigLine;
import kz.greetgo.conf2.lines.ConfigLineKeyValue;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OneFileReaderTest {

  private List<ConfigLine> rndFileContent() {
    List<ConfigLine> list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      list.add(new ConfigLineKeyValue(RND.str(10), RND.str(13), true));
    }
    return list;
  }

  @Test
  public void content__callTwice__butReadFsContentOnce() {

    Calendar time = new GregorianCalendar();

    FileSystemAccessForTests fs = new FileSystemAccessForTests();
    fs.nowSupplier = time::getTime;

    String path = RND.str(10) + '/' + RND.str(10);

    List<ConfigLine> content = rndFileContent();

    fs.writeFile(path, content);

    OneFileReader oneFileReader = new OneFileReader(path, fs, null, () -> 300);

    //
    //
    List<ConfigLine> actualContent1 = oneFileReader.content();
    //
    //

    time.add(Calendar.MILLISECOND, 200);

    //
    //
    List<ConfigLine> actualContent2 = oneFileReader.content();
    //
    //

    assertThat(actualContent1).isEqualTo(content);
    assertThat(actualContent2).isEqualTo(content);

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(1);
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(1);

  }

  @Test
  public void content__вызываемДважды__иОбращениеКФайловойСистемеДалаетсяТожеДваждыПотомуЧтоПрошёл_delayBetweenReadMs() {

    Calendar time = new GregorianCalendar();

    FileSystemAccessForTests fs = new FileSystemAccessForTests();
    fs.nowSupplier = time::getTime;

    String path = RND.str(10) + '/' + RND.str(10);

    List<ConfigLine> content = rndFileContent();

    fs.writeFile(path, content);

    OneFileReader oneFileReader = new OneFileReader(path, fs, null, () -> 300);

    //
    //
    List<ConfigLine> actualContent1 = oneFileReader.content();
    //
    //

    time.add(Calendar.MILLISECOND, 2000);

    //
    //
    List<ConfigLine> actualContent2 = oneFileReader.content();
    //
    //

    assertThat(actualContent1).isEqualTo(content);
    assertThat(actualContent2).isEqualTo(content);

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(1);
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(2);

  }
}