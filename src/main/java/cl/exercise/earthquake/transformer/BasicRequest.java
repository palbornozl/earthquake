package cl.exercise.earthquake.transformer;

import static cl.exercise.earthquake.utils.Utils.DATE_FORMAT_YMD;

import cl.exercise.earthquake.exception.validator.DateConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(builderClassName = "BasicRequestBuilder", toBuilder = true)
@JsonDeserialize(builder = BasicRequest.BasicRequestBuilder.class)
@ToString(callSuper = true)
public class BasicRequest {

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
  @Min(value = 0, message = "magnitudeMinima debe ser [0,10]")
  @Max(value = 10, message = "magnitudeMinima debe ser [0,10]")
  @NotNull(message = "magnitudeMinima campo obligatorio")
  private Float magnitudeMin;

  @JsonProperty("magnitudeMaxima")
  @JsonFormat(shape = Shape.NUMBER_FLOAT)
  @Min(value = 0, message = "magnitudeMaxima debe ser [0,10]")
  @Max(value = 10, message = "magnitudeMaxima debe ser [0,10]")
  private Float magnitudeMax;
}
