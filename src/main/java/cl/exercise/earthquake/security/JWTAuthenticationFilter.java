package cl.exercise.earthquake.security;

import static cl.exercise.earthquake.security.JWTConstants.EXPIRATION_TIME;
import static cl.exercise.earthquake.security.JWTConstants.HEADER_STRING;
import static cl.exercise.earthquake.security.JWTConstants.SECRET_PASSWORD;
import static cl.exercise.earthquake.security.JWTConstants.TOKEN_PREFIX;
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
  private final AuthenticationManager authenticationManager;

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
            .sign(HMAC512(SECRET_PASSWORD.getBytes()));

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode result = mapper.createObjectNode();
    result.put("user", ((User) auth.getPrincipal()).getUsername());
    result.put("token", token);

    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    res.getWriter().write(result.toPrettyString());
    res.getWriter().flush();
  }
}
