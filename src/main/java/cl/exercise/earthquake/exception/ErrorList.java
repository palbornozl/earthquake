package cl.exercise.earthquake.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorList {
  private List<ErrorDetails> errors;
}
