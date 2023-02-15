package cl.exercise.earthquake.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@Builder(builderClassName = "EarthquakeApiResponseBuilder", toBuilder = true)
@JsonDeserialize(builder = EarthquakeApiResponse.EarthquakeApiResponseBuilder.class)
@ToString(callSuper = true)
public class EarthquakeApiResponse {

  @JsonProperty("mag")
  private Double magnitude;

  @JsonProperty("place")
  private String place;

  @JsonProperty("time")
  private String time;

  @JsonProperty("updated")
  private String updated;

  @JsonProperty("alert")
  private String alert;

  @JsonProperty("status")
  private String status;

  @JsonProperty("tsunami")
  private Integer tsunami;

  @JsonProperty("magType")
  private String magnitudeType;

  @JsonProperty("type")
  private String type;

  @JsonProperty("title")
  private String title;
}
