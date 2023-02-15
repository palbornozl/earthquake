package cl.exercise.earthquake.dto;

import static cl.exercise.earthquake.utils.Utils.DATE_FORMAT_YMD;

import cl.exercise.earthquake.exception.validator.DateConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EarthquakeResponse {
  @JsonProperty("identify")
  private String id;

  @JsonProperty("fechaConsulta")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_YMD)
  @DateConstraint
  private String createdAt;

  @JsonProperty("origen")
  private String origen;
  @JsonProperty("observacion")
  private String observacion;

  @JsonProperty("fechaInicio")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_YMD)
  @DateConstraint
  private String fechaInicio;

  @JsonProperty("fechaFin")
  @JsonFormat(shape = Shape.STRING, pattern = DATE_FORMAT_YMD)
  @DateConstraint
  private String fechaFin;

  @JsonProperty(value = "magnitudeMinima")
  @JsonFormat(shape = Shape.NUMBER_FLOAT)
  private Float magnitudeMin;

  @JsonProperty("magnitudeMaxima")
  @JsonFormat(shape = Shape.NUMBER_FLOAT)
  private Float magnitudeMax;

  @JsonProperty("token_origen")
  private String token;

}
