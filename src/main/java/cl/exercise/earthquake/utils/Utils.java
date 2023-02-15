package cl.exercise.earthquake.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

  public static final String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

  public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

  public static final String DATE_FORMAT_COMPLETE = "EEEE, MMMM dd, yyyy hh:mm:ss.SSS a";

  Utils() {
  }

  @SneakyThrows
  public static boolean validatesDates(String init, String end) {
    SimpleDateFormat sdformat = new SimpleDateFormat(DATE_FORMAT_YMD);
    Date dateInit = sdformat.parse(init);
    Date dateEnd = sdformat.parse(end);
    log.debug("The date 1 is: " + sdformat.format(dateInit));
    log.debug("The date 2 is: " + sdformat.format(dateEnd));
    if (dateInit.compareTo(dateEnd) > 0) {
      log.error("Date 1 occurs after Date 2");
      return false;
    } else if (dateInit.compareTo(dateEnd) < 0) {
      log.debug("Date 1 occurs before Date 2");
    } else if (dateInit.compareTo(dateEnd) == 0) {
      log.debug("Both dates are equal");
    }
    return true;
  }

  @SneakyThrows
  public static String getDateFormatCompleteToString(Date date) {
    return new SimpleDateFormat(DATE_FORMAT_COMPLETE).format(date);
  }

  @SneakyThrows
  public static Timestamp getStringToDateFormatComplete(String dateString) {
    return new Timestamp((new SimpleDateFormat(DATE_FORMAT_YMD)).parse(dateString).getTime());
  }

  @SneakyThrows
  public static boolean isJSONValid(String jsonInString) {
    new ObjectMapper().readTree(jsonInString);
    return true;
  }
}
