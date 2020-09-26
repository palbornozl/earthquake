package cl.exercise.earthquake.security;

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
      User user = new User(
          USER_NAME,
          "$2y$12$8j5UDuBj/3FtV.TlOKUV3.vFP.6VccAJX9/ntV/PNGS.0gjgXlDLa",
          new ArrayList<>());
      return user;
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
