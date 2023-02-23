package cl.exercise.earthquake.service;

import cl.exercise.earthquake.dto.EarthquakeRequest;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EarthquakeApiService {

  private final WebClient webClient;

  public EarthquakeApiService(WebClient webClient) {
    this.webClient = webClient;
  }

  @SneakyThrows
  public JsonNode callEarthquakeByDateAndMinMagnitude(EarthquakeRequest request) {
    MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
    parameters.add("starttime", request.getFechaInicio());
    parameters.add("endtime", request.getFechaFin());
    parameters.add("minmagnitude", request.getMagnitudeMin().toString());

    return callEarthquakeApi(parameters);
  }

  @SneakyThrows
  public JsonNode callEarthquakeByMagnitudes(EarthquakeRequest request) {
    MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
    parameters.add("minmagnitude", request.getMagnitudeMin().toString());
    parameters.add("maxmagnitude", request.getMagnitudeMax().toString());

    return callEarthquakeApi(parameters);
  }

  private JsonNode callEarthquakeApi(MultiValueMap<String,String> parameters) {
    StopWatch watch = new StopWatch();
    log.info("Call earthquake {}", parameters);
    watch.start();

    JsonNode response =
        this.webClient
            .get()
            .uri(uriBuilder -> {
              URI ub = uriBuilder.queryParams(parameters).build();
              log.debug(">>> {} -- {} -- {}", ub.getPath(), ub.getQuery(), ub);
              return ub;
            })
            .retrieve()
            .onStatus(
                HttpStatus::is4xxClientError,
                responseError -> {
                  log.error("4xx error");
                  return Mono.error(new RuntimeException("4xx"));
                })
            .onStatus(
                HttpStatus::is5xxServerError,
                responseError -> {
                  log.error("5xx error");
                  return Mono.error(new RuntimeException("5xx"));
                })
            .bodyToMono(JsonNode.class)
            .block();
    watch.stop();
    log.info("End Call earthquake {}ms", watch.getTotalTimeMillis());

    return response;
  }
}
