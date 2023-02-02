package kz.greetgo.conf2.postgres;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private       String tableName;
    private final String colId        = "id";
    private final String colFilename  = "filename";
    private final String colLineNo    = "num";
    private final String colLineType  = "lineType";
    private final String colKey       = "key";
    private final String colValue     = "value";
    private final String colCommented = "commented";

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
      return new FileSystemAccessPostgres(this);
    }

    public static Builder of() {
      return new Builder();
    }
  }

  private FileSystemAccessPostgres(Builder builder) {
    this.builder = builder;
  }

  @SneakyThrows
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
    try (Connection connection = builder.dataSource.getConnection()) {
      return connection.getMetaData().getTables(null, null, builder.tableName, null).next();
    } catch (Exception exception) {
      return false;
    }
  }

  @SneakyThrows
  private void init() {
    try (Connection connection = builder.dataSource.getConnection()) {

      try (Statement statement = connection.createStatement()) {

        statement.execute("create table " + builder.tableName + " ("
                            + builder.colId + " varchar(30) primary key,"
                            + builder.colFilename + " varchar(300) not null,"
                            + builder.colLineNo + " numeric not null,"
                            + builder.colLineType + " varchar(300) not null,"
                            + builder.colKey + " varchar(300),"
                            + builder.colValue + " varchar(3000),"
                            + builder.colCommented + " varchar(30)"
                            + ")");

      }

    }
  }

  @SneakyThrows
  @Override
  public void writeFile(String path, List<ConfigLine> lines) {
    checkInit();
    try (Connection connection = builder.dataSource.getConnection()) {

      try (Statement statement = connection.createStatement()) {

        statement.execute("insert into " + builder.tableName + " values("
                            + UUID.randomUUID() + ","
                            + builder.colFilename + ","
                            + builder.colLineNo + ","
                            + builder.colLineType + ","
                            + builder.colKey + ","
                            + builder.colValue + ","
                            + builder.colCommented
                            + ")");

      }

    }
  }
}
