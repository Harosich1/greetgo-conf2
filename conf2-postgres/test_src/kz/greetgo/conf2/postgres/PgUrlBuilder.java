package kz.greetgo.conf2.postgres;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PgUrlBuilder {
  private final String host;
  private final int    port;
  private final String dbName;

  public static PgUrlBuilder on(String host, int port, String dbName) {
    return new PgUrlBuilder(host, port, dbName);
  }

  public String build() {

    String hostPort = Arrays.stream(host.split(","))
                            .map(String::trim)
                            .map(s -> s + ":" + port)
                            .collect(joining(","));

    return "jdbc:postgresql://" + hostPort + "/" + dbName;
  }
}
