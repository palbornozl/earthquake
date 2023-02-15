package cl.exercise.earthquake.service;

import cl.exercise.earthquake.dto.EarthquakeApiResponse;
import cl.exercise.earthquake.dto.EarthquakeRequest;
import cl.exercise.earthquake.dto.EarthquakeResponse;
import cl.exercise.earthquake.message.producer.ProducerService;
import cl.exercise.earthquake.repository.EarthquakeRepository;
import cl.exercise.earthquake.transformer.ResponseTransformer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class EarthquakeInternalService {

  final EarthquakeApiService apiService;

  final ResponseTransformer transformer;

  final EarthquakeRepository repository;

  final ProducerService producer;

  public EarthquakeInternalService(
      EarthquakeApiService apiService,
      ResponseTransformer transformer,
      EarthquakeRepository repository,
      ProducerService producer) {
    this.apiService = apiService;
    this.transformer = transformer;
    this.repository = repository;
    this.producer = producer;
  }

  public List<EarthquakeApiResponse> getInfoByDateAndMinMagnitude(EarthquakeRequest request) {
    List<Map<String, Object>> result =
        (new ObjectMapper())
            .convertValue(
                apiService.callEarthquakeByDateAndMinMagnitude(request).get("features"),
                new TypeReference<>() {});

    List<EarthquakeApiResponse> response =
        transformer.transformApiResponseToEarthquakeApiResponse(result);
    sendResponse(request, response);
    return response;
  }

  public List<EarthquakeApiResponse> getInfoByMagnitudes(EarthquakeRequest request) {
    List<Map<String, Object>> result =
        (new ObjectMapper())
            .convertValue(
                apiService.callEarthquakeByMagnitudes(request).get("features"),
                new TypeReference<>() {});

    List<EarthquakeApiResponse> response =
        transformer.transformApiResponseToEarthquakeApiResponse(result);
    sendResponse(request, response);
    return response;
  }

  @Async
  @SneakyThrows
  void sendResponse(EarthquakeRequest request, List<EarthquakeApiResponse> response) {
    if (!CollectionUtils.isEmpty(response)) {
      save(request, response);
      response.forEach(e -> producer.sendMessage(request, e));
    }
  }

  @SneakyThrows
  private void save(EarthquakeRequest request, List<EarthquakeApiResponse> responses) {
    log.debug("--> saving {}", request.toString());
    repository.save(transformer.transformRequestToEarthquakeModel(request,responses));
  }

  public List<EarthquakeResponse> getAll(){
    log.debug("-->getting all");
    List<EarthquakeResponse> earthquakeResponses = new LinkedList<>();
    repository.findAll().forEach(m -> earthquakeResponses.add(
        EarthquakeResponse.builder()
            .createdAt(m.getCreatedAt().toString())
            .fechaInicio(m.getFechaInicio().toString())
            .fechaFin(m.getFechaFin().toString())
            .magnitudeMin(m.getMagnitudMin())
            .magnitudeMax(m.getMagnitudMax())
            .observacion(m.getObservacion())
            .origen(m.getOrigen())
            .build()
    ));
    return earthquakeResponses;
  }
}
