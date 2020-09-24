package cl.exercise.earthquake.exception.validator;

import static cl.exercise.earthquake.utils.Utils.DATE_FORMAT_YMD;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class DateValidator implements ConstraintValidator<DateConstraint, String> {

  @Override
  public void initialize(DateConstraint dateConstraint) {}

  @Override
  @SneakyThrows
  public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
    if (StringUtils.isEmpty(date)) return true;
    try {
      Date isValid = new SimpleDateFormat(DATE_FORMAT_YMD).parse(date);
      return true;
    } catch (Exception e) {
      log.error("Error con el formato de fecha {} - {}", DATE_FORMAT_YMD, date);
      return false;
    }
  }
}
