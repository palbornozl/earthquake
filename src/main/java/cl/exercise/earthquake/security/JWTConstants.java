package cl.exercise.earthquake.security;

public class JWTConstants {
  public static final long EXPIRATION_TIME = 900_000;
  public static final String SECRET_PASSWORD =
      "$2y$12$SffLIjurvLJ3WwFHmbalne3TSaXTlZELCU.0Mlp9lPhLC8wr1FKi2"; // my5ecre7P@ssw0rd
  public static final String HEADER_STRING = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  JWTConstants() {
  }
}
