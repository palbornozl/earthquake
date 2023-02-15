package cl.exercise.earthquake.transformer;

import static cl.exercise.earthquake.utils.Utils.getDateFormatCompleteToString;
import static cl.exercise.earthquake.utils.Utils.getStringToDateFormatComplete;

import cl.exercise.earthquake.dto.EarthquakeApiResponse;
import cl.exercise.earthquake.dto.EarthquakeRequest;
import cl.exercise.earthquake.model.EarthquakeModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class ResponseTransformer {

  public static final String TSUNAMI = "tsunami";

  @SneakyThrows
  public List<EarthquakeApiResponse> transformApiResponseToEarthquakeApiResponse(
      List<Map<String, Object>> result) {
    List<EarthquakeApiResponse> responses = new LinkedList<>();
    result.forEach(
        l -> {
          Map<String, Object> map = (Map<String, Object>) l.get("properties");
          log.debug("--> {}", map.toString());
          responses.add(
              EarthquakeApiResponse.builder()
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
                      map.get(TSUNAMI).getClass().getSimpleName().equalsIgnoreCase("Integer")
                          ? (Integer) map.get(TSUNAMI)
                          : ((Double) map.get(TSUNAMI)).intValue())
                  .magnitudeType((String) map.get("magType"))
                  .type((String) map.get("type"))
                  .title((String) map.get("title"))
                  .build());
        });
    return responses;
  }
  @SneakyThrows
  public EarthquakeModel transformRequestToEarthquakeModel(EarthquakeRequest request, Object responses, String origen){
    return EarthquakeModel.builder()
        .fechaInicio(
            StringUtils.isEmpty(request.getFechaInicio())
                ? null
                : getStringToDateFormatComplete(request.getFechaInicio()))
        .fechaFin(
            StringUtils.isEmpty(request.getFechaFin())
                ? null
                : getStringToDateFormatComplete(request.getFechaFin()))
        .origen(origen.toUpperCase(Locale.ENGLISH))
        .observacion(
            StringUtils.isEmpty(request.getFechaInicio())
                ? origen.toUpperCase(Locale.ENGLISH) + " Buscando magnitudes"
                : origen.toUpperCase(Locale.ENGLISH) + " Buscando por fechas y magnitude min")
        .magnitudMin(request.getMagnitudeMin())
        .magnitudMax(request.getMagnitudeMax())
        .salida(new ObjectMapper().writeValueAsString(responses))
        .token(request.getUserToken())
        .build();
  }

}
