package cl.exercise.earthquake.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import cl.exercise.earthquake.security.model.UserLogin;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;

  public static final String SECRET =
      "$2y$12$SffLIjurvLJ3WwFHmbalne3TSaXTlZELCU.0Mlp9lPhLC8wr1FKi2"; // my5ecre7P@ssw0rd

  public static final String TOKEN_PREFIX = "Bearer ";

  public static final String HEADER_STRING = "Authorization";

  public static final long EXPIRATION_TIME = 4_000_000;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    setFilterProcessesUrl("/authenticate");
  }

  @Override
  @SneakyThrows
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
    UserLogin creds = new ObjectMapper().readValue(req.getInputStream(), UserLogin.class);

    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            creds.getUsername(), creds.getPassword(), new ArrayList<>()));
  }

  @Override
  @SneakyThrows
  protected void successfulAuthentication(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {

    String token =
        JWT.create()
            .withSubject(((User) auth.getPrincipal()).getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode result = mapper.createObjectNode();
    result.put("user", ((User) auth.getPrincipal()).getUsername());
    result.put("token", token);

    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    res.getWriter().write(result.toPrettyString());
    res.getWriter().flush();
  }
}
