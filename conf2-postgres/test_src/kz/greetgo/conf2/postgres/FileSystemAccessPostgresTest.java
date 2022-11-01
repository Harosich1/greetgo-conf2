package kz.greetgo.conf2.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import javax.sql.DataSource;
import kz.greetgo.conf2.FileReader;
import kz.greetgo.util.RND;
import lombok.SneakyThrows;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemAccessPostgresTest {

  protected DataSource               dataSource;
  protected FileSystemAccessPostgres fsa;

  @BeforeMethod
  public void connectToDb() {

    PgUrlBuilder urlBuilder = PgUrlBuilder.on("localhost", 21001, "conf2");

    dataSource = new DataSourceForTests(urlBuilder.build(), "conf2", "nRc90iV1fWU0kAg1c0aFIrbQk1ilUn");

    fsa = FileSystemAccessPostgres.Builder.of()
                                          .dataSource(dataSource)
                                          .tableName("fsa-" + RND.strEng(13))
                                          .build();
  }

  @Test
  @SneakyThrows
  public void connectionTest() {

    try (Connection connection = dataSource.getConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("select 123")) {
        try (ResultSet rs = ps.executeQuery()) {
          if (!rs.next()) {
            throw new RuntimeException("aHACoVzEBv");
          }

          assertThat(rs.getInt(1)).isEqualTo(123);
        }
      }
    }

  }

  @Test
  @SneakyThrows
  public void name() {

    Optional<FileReader> asd = fsa.readFile("some-file");

    assertThat(asd).isEmpty();

  }
}
