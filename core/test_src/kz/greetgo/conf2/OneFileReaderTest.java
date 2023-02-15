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

    time.add(Calendar.MILLISECOND, 10);

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

    int nThreads = 10;

    ExecutorService service = Executors.newFixedThreadPool(nThreads);
    CountDownLatch  latch   = new CountDownLatch(nThreads);

    final AtomicReference<List<ConfigLine>> actualContent1 = new AtomicReference<>();


    AtomicReference<OneFileReader> fileReader = new AtomicReference<>();
    fileReader.set(new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis));

    Runnable task = () -> {
      //
      //
      actualContent1.set(fileReader.get().content());
      //
      //

      latch.countDown();
    };

    for (int i = 0; i < nThreads; i++) {
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

    int nThreads = 10;

    ExecutorService service = Executors.newFixedThreadPool(nThreads);
    CountDownLatch  latch   = new CountDownLatch(nThreads);


    final AtomicReference<List<ConfigLine>> actualContent1 = new AtomicReference<>();

    AtomicReference<OneFileReader> fileReader = new AtomicReference<>();
    fileReader.set(new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis));

    Runnable task = () -> {

        //
        //
        actualContent1.set(fileReader.get().content());
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

    assertThat(fs.allFiles.get(path).contentReadCount).isEqualTo(10);
    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(10);

  }

  @Test
  public void content__concurrent__callMultiTimes__lastModifiedTimeOnce() throws InterruptedException {

    Calendar time = new GregorianCalendar();

    FileSystemAccessForTests fs = new FileSystemAccessForTests();
    fs.nowSupplier = time::getTime;

    String path = RND.str(10) + '/' + RND.str(10);

    List<ConfigLine> content = rndFileContent();

    fs.writeFile(path, content);

    int nThreads = 10;

    ExecutorService service = Executors.newFixedThreadPool(nThreads);
    CountDownLatch  latch   = new CountDownLatch(nThreads);

    final AtomicReference<List<ConfigLine>> actualContent1 = new AtomicReference<>();

    AtomicReference<OneFileReader> fileReader = new AtomicReference<>();
    fileReader.set(new OneFileReader(path, fs, null, () -> 300, time::getTimeInMillis));

    Runnable task = () -> {

        //
        //
        actualContent1.set(fileReader.get().content());
        //
        //

        time.add(Calendar.MILLISECOND, 2000);

      latch.countDown();
    };

    for (int i = 0; i < nThreads; i++) {
      service.submit(task);
    }
    latch.await();
    assertThat(actualContent1.get()).isEqualTo(content);

    assertThat(fs.allFiles.get(path).lastModifiedAtCallCount).isEqualTo(1);

  }

}
