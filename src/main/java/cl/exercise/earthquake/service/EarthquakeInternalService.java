package cl.exercise.earthquake.service;

import static cl.exercise.earthquake.utils.Utils.getStringToDateFormatComplete;

import cl.exercise.earthquake.model.EarthquakeModel;
import cl.exercise.earthquake.repository.EarthquakeRepository;
import cl.exercise.earthquake.transformer.BasicRequest;
import cl.exercise.earthquake.transformer.EarthquakeResponse;
import cl.exercise.earthquake.transformer.ResponseTransformer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class EarthquakeInternalService {

  final EarthquakeApiService apiService;

  final ResponseTransformer transformer;

  final EarthquakeRepository repository;

  public EarthquakeInternalService(
      EarthquakeApiService apiService,
      ResponseTransformer transformer,
      EarthquakeRepository repository) {
    this.apiService = apiService;
    this.transformer = transformer;
    this.repository = repository;
  }

  /** TODO: hacer insert en BD, hacer insert en BD, hacer insert en redis, hacer insert producer */
  public List<EarthquakeResponse> getInfoByDateAndMinMagnitude(BasicRequest request) {
    List<Map<String, Object>> result =
        (new ObjectMapper())
            .convertValue(
                apiService.callEarthquakeByDateAndMinMagnitude(request).get("features"),
                new TypeReference<List<Map<String, Object>>>() {});

    List<EarthquakeResponse> response =
        transformer.transformApiResponseToEarthquakeResponse(result);

    save(request, response);

    return response;
  }

  /** TODO: hacer insert en BD, hacer insert en redis, hacer insert producer */
  public List<EarthquakeResponse> getInfoByMagnitudes(BasicRequest request) {
    List<Map<String, Object>> result =
        (new ObjectMapper())
            .convertValue(
                apiService.callEarthquakeByMagnitudes(request).get("features"),
                new TypeReference<List<Map<String, Object>>>() {});

    List<EarthquakeResponse> response =
        transformer.transformApiResponseToEarthquakeResponse(result);

    save(request, response);

    return response;
  }

  @SneakyThrows
  private void save(BasicRequest request, List<EarthquakeResponse> responses) {
    log.debug("--> saving {}", request.toString());
    repository.save(
        EarthquakeModel.builder()
            .fechaInicio(StringUtils.isEmpty(request.getFechaInicio()) ? null : getStringToDateFormatComplete(request.getFechaInicio()))
            .fechaFin(StringUtils.isEmpty(request.getFechaFin()) ? null : getStringToDateFormatComplete(request.getFechaFin()))
            .campo(StringUtils.isEmpty(request.getFechaInicio()) ? "Buscando magnitudes" : "Buscando por fechas y magnitude min")
            .magnitudMin(request.getMagnitudeMin())
            .magnitudMax(request.getMagnitudeMax())
            .salida(new ObjectMapper().writeValueAsString(responses))
            .build());
  }
}
