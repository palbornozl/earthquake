package cl.exercise.earthquake.controller;

import static cl.exercise.earthquake.security.JWTConstants.HEADER_STRING;
import static cl.exercise.earthquake.security.JWTConstants.TOKEN_PREFIX;
import static cl.exercise.earthquake.utils.Utils.DATE_FORMAT_YMD;
import static cl.exercise.earthquake.utils.Utils.validatesDates;

import cl.exercise.earthquake.dto.EarthquakeApiResponse;
import cl.exercise.earthquake.dto.EarthquakeRequest;
import cl.exercise.earthquake.dto.EarthquakeResponse;
import cl.exercise.earthquake.service.EarthquakeInternalService;
import java.util.List;
import javax.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/search")
@Transactional(transactionManager = "transactionManagerEarthquake")
public class EarthquakeController {
  @Autowired private EarthquakeInternalService service;

  @SneakyThrows
  @GetMapping("/all")
  public ResponseEntity<List<EarthquakeResponse>> getAll() {

    List<EarthquakeResponse> response = service.getAll();
    return new ResponseEntity<>(response, response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @SneakyThrows
  @PostMapping("/dates")
  public ResponseEntity<List<EarthquakeApiResponse>> findByDates(
      @Valid @RequestBody EarthquakeRequest request, BindingResult result,
      @RequestHeader(value = HEADER_STRING) String token) {
    if (StringUtils.isEmpty(request.getFechaInicio())
        || StringUtils.isEmpty(request.getFechaFin())
        || result.hasErrors()) {
      throw new IllegalArgumentException(
          "[Error] Fecha con formato incorrecto ("
              + DATE_FORMAT_YMD
              + ") fechaInicio: "
              + request.getFechaInicio()
              + " fechaFin: "
              + request.getFechaFin());
    } else if (!validatesDates(request.getFechaInicio(), request.getFechaFin())) {
      throw new IllegalArgumentException(
          "[Error] Fecha inicio: "
              + request.getFechaInicio()
              + " es mayor que fechaFin: "
              + request.getFechaFin());
    }
    request.setUserToken(token.replace(TOKEN_PREFIX, "").trim());
    List<EarthquakeApiResponse> response = service.getInfoByDateAndMinMagnitude(request);
    return new ResponseEntity<>(response, response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @SneakyThrows
  @PostMapping("/magnitudes")
  public ResponseEntity<List<EarthquakeApiResponse>> findByMagnitudes(
      @Valid @RequestBody EarthquakeRequest request,
      @RequestHeader(value = HEADER_STRING) String token) {

    if (request.getMagnitudeMin() > request.getMagnitudeMax())
      throw new IllegalArgumentException("[Error] Magnitud Min es mayor que Magnitude Max");

    request.setUserToken(token.replace(TOKEN_PREFIX, "").trim());
    List<EarthquakeApiResponse> response = service.getInfoByMagnitudes(request);
    return new ResponseEntity<>(response, response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }
}
