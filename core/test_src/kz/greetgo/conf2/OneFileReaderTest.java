package kz.greetgo.conf2;

import for_tests.FileSystemAccessForTests;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
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

    OneFileReader oneFileReader = new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis);

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

    OneFileReader oneFileReader = new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis);

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

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(2);
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(2);

  }

  @Test
  public void content__when__file___is__not__present() {

    Calendar time = new GregorianCalendar();

    FileSystemAccessForTests fs = new FileSystemAccessForTests();
    fs.nowSupplier = time::getTime;

    String path = RND.str(10) + '/' + RND.str(10);

    Supplier<List<ConfigLine>> defaultContentSupplier = () -> List.of(new ConfigLineKeyValue("dg", "sdf", false));

    OneFileReader oneFileReader = new OneFileReader(path, fs, defaultContentSupplier, () -> 300, time::getTimeInMillis);

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

    assertThat(actualContent1).isEqualTo(defaultContentSupplier.get());
    assertThat(actualContent2).isEqualTo(defaultContentSupplier.get());

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(1);
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(1);

  }

  @Test
  public void content__concurrent__callAndReturn__ContentCounterOneTime() throws InterruptedException {

    Calendar time = new GregorianCalendar();

    FileSystemAccessForTests fs = new FileSystemAccessForTests();
    fs.nowSupplier = time::getTime;

    String path = RND.str(10) + '/' + RND.str(10);

    List<ConfigLine> content = rndFileContent();

    fs.writeFile(path, content);

    ExecutorService service = Executors.newFixedThreadPool(10);//TODO тут 10 нужно вынести в константу - она несколько раз встречается
    CountDownLatch  latch   = new CountDownLatch(10);

    final AtomicReference<List<ConfigLine>> actualContent1 = new AtomicReference<>();

    // TODO эта переменная не нужно - c fileReader можно напрямую
    final AtomicReference<OneFileReader> oneFileReader = new AtomicReference<>();

    OneFileReader fileReader = new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis);

    Runnable task = () -> {
      oneFileReader.set(fileReader);

      //
      //
      actualContent1.set(oneFileReader.get().content());
      //
      //

      latch.countDown();
    };

    for (int i = 0; i < 10; i++) {
      service.submit(task);
    }
    latch.await();
    assertThat(actualContent1.get()).isEqualTo(content);

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(1);
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(1);

  }

  @Test
  public void content__concurrent__callAndReturn__ContentCounterEqualToCallNumbers() throws InterruptedException {

    Calendar time = new GregorianCalendar();

    FileSystemAccessForTests fs = new FileSystemAccessForTests();
    fs.nowSupplier = time::getTime;

    String path = RND.str(10) + '/' + RND.str(10);

    List<ConfigLine> content = rndFileContent();

    fs.writeFile(path, content);

    ExecutorService service = Executors.newFixedThreadPool(10);//TODO тут 10 нужно вынести в константу - она несколько раз встречается
    CountDownLatch  latch   = new CountDownLatch(10);

    final AtomicReference<List<ConfigLine>> actualContent1 = new AtomicReference<>();

    OneFileReader fileReader = new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis);

    Runnable task = () -> {

      //
      //
      actualContent1.set(fileReader.content());
      //
      //

      time.add(Calendar.MILLISECOND, 2000);

      latch.countDown();
    };

    for (int i = 0; i < 10; i++) {
      service.submit(task);
    }
    latch.await();
    assertThat(actualContent1.get()).isEqualTo(content);

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(10);// TODO нужно переделать тест, чтобы это вызвалось 1 раз,
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(10);// TODO а это - 10

  }

  // TODO написать остальные тесты. Главный тест: проверяет, чтобы значение по умолчанию формировалось один раз

}
