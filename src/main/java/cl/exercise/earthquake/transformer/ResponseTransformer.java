package cl.exercise.earthquake.transformer;

import static cl.exercise.earthquake.utils.Utils.getDateFormatCompleteToString;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseTransformer {

  @SneakyThrows
  public List<EarthquakeResponse> transformApiResponseToEarthquakeResponse(
      List<Map<String, Object>> result) {
    List<EarthquakeResponse> responses = new LinkedList<>();
    result.forEach(
        l -> {
          Map<String, Object> map = (Map<String, Object>) l.get("properties");
          log.debug("--> {}", map.toString());
          responses.add(
              EarthquakeResponse.builder()
                  .magnitude(
                      map.get("mag").getClass().getSimpleName().equalsIgnoreCase("Double")
                          ? (Double) map.get("mag")
                          : ((Integer) map.get("mag")).doubleValue())
                  .place((String) map.get("place"))
                  .time(getDateFormatCompleteToString(new Date((Long) map.get("time"))))
                  .updated(getDateFormatCompleteToString(new Date((Long) map.get("updated"))))
                  .alert((String) map.get("alert"))
                  .status((String) map.get("status"))
                  .tsunami(
                      map.get("tsunami").getClass().getSimpleName().equalsIgnoreCase("Integer")
                          ? (Integer) map.get("tsunami")
                          : ((Double) map.get("tsunami")).intValue())
                  .magnitudeType((String) map.get("magType"))
                  .type((String) map.get("type"))
                  .title((String) map.get("title"))
                  .build());
        });
    return responses;
  }
}
