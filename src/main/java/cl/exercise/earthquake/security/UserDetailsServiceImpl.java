package cl.exercise.earthquake.security;

import static cl.exercise.earthquake.security.JWTConstants.SECRET_PASSWORD;

import java.util.ArrayList;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String USER_NAME = "admin";

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (USER_NAME.equals(username)) {
      return new User(USER_NAME,SECRET_PASSWORD,new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
