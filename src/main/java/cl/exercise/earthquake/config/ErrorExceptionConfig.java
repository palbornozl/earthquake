package cl.exercise.earthquake.config;

import cl.exercise.earthquake.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorExceptionConfig {
  @Bean
  public GlobalExceptionHandler getExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}
