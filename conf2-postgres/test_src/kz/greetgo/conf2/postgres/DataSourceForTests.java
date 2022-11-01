package kz.greetgo.conf2.postgres;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import lombok.SneakyThrows;

public class DataSourceForTests implements DataSource {

  private final String url;
  private final String username;
  private final String password;

  public DataSourceForTests(String url, String username, String password) {
    this.url      = url;
    this.username = username;
    this.password = password;
  }

  @Override
  @SneakyThrows
  public Connection getConnection() throws SQLException {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(url, username, password);
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.getConnection");
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.getLogWriter");
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.setLogWriter");
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.setLoginTimeout");
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.getLoginTimeout");
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.getParentLogger");
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.unwrap");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new RuntimeException("01.11.2022 14:05: Not impl yet: DataSourceForTests.isWrapperFor");
  }
}
