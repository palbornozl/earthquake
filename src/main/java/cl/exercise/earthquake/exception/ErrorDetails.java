package cl.exercise.earthquake.exception;

import static cl.exercise.earthquake.utils.Utils.DATE_FORMAT_YMD_HMS;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorDetails {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_YMD_HMS)
  private LocalDateTime timestamp;

  private int status;

  private String title;

  private String detail;

  private String source;

  @Override
  public String toString() {
    return "ErrorDetails{"
        + "timestamp="
        + timestamp
        + ", status="
        + status
        + ", title='"
        + title
        + '\''
        + ", detail='"
        + detail
        + '\''
        + ", source='"
        + source
        + '\''
        + '}';
  }
}
