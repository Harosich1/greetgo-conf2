package kz.greetgo.conf2.postgres;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.DataSource;
import kz.greetgo.conf2.FileReader;
import kz.greetgo.conf2.FileSystemAccess;
import kz.greetgo.conf2.lines.ConfigLine;
import lombok.SneakyThrows;

import static java.util.Objects.requireNonNull;

public class FileSystemAccessPostgres implements FileSystemAccess {

  private final Builder builder;

  public static class Builder {
    private DataSource dataSource;

    private String tableName;
    private String colId        = "id";
    private String colFilename  = "filename";
    private String colLineNo    = "num";
    private String colLineType  = "lineType";
    private String colKey       = "key";
    private String colValue     = "value";
    private String colCommented = "commented";

    public Builder dataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
    }

    public Builder tableName(String tableName) {
      this.tableName = tableName;
      return this;
    }

    public FileSystemAccessPostgres build() {
      requireNonNull(dataSource, "h4OZ3hQ5Bm :: dataSource");
      // TODO artyom добить тудушки
      return new FileSystemAccessPostgres(this);
    }

    public static Builder of() {
      return new Builder();
    }
  }

  private FileSystemAccessPostgres(Builder builder) {
    this.builder = builder;
  }

  @Override
  public Optional<FileReader> readFile(String path) {
    checkInit();
    throw new RuntimeException("01.11.2022 13:30: Not impl yet: FileSystemAccessPostgres.readFile");
  }

  private final AtomicBoolean isInitiated = new AtomicBoolean(false);

  private void checkInit() {
    if (isInitiated.get()) {
      return;
    }
    synchronized (this) {
      if (isInitiated.get()) {
        return;
      }

      if (isInitiatedInDb()) {
        isInitiated.set(true);
        return;
      }

      init();
      isInitiated.set(true);
    }
  }

  private boolean isInitiatedInDb() {
    throw new RuntimeException("01.11.2022 13:47: Not impl yet: FileSystemAccessPostgres.checkInitInDb");
  }

  @SneakyThrows
  private void init() {
    try (Connection connection = builder.dataSource.getConnection()) {

      try (Statement statement = connection.createStatement()) {

        statement.execute("create table " + builder.tableName + " ("
                            + builder.colId + " varchar(30) primary key,"
                            + builder.colFilename + " varchar(300) not null,"
                            // TODO добить
                            + ")");

      }

    }
  }

  @Override
  public void writeFile(String path, List<ConfigLine> lines) {
    checkInit();
    throw new RuntimeException("01.11.2022 13:30: Not impl yet: FileSystemAccessPostgres.writeFile");
  }
}
