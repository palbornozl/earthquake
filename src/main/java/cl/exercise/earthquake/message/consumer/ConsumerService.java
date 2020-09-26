package cl.exercise.earthquake.message.consumer;

import static cl.exercise.earthquake.utils.Utils.getStringToDateFormatComplete;
import static cl.exercise.earthquake.utils.Utils.isJSONValid;

import cl.exercise.earthquake.model.EarthquakeModel;
import cl.exercise.earthquake.repository.EarthquakeRepository;
import cl.exercise.earthquake.transformer.BasicRequest;
import cl.exercise.earthquake.transformer.EarthquakeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@PropertySource({"classpath:kafka-${spring.profiles.active:default}.properties"})
@EnableKafka
@Slf4j
public final class ConsumerService {
  final EarthquakeRepository repository;

  public ConsumerService(EarthquakeRepository repository) {
    this.repository = repository;
  }

  @KafkaListener(
      topics = "#{'${spring.kafka.template.default-topic}'}",
      groupId = "#{'${spring.kafka.consumer.group-id}'}")
  @SneakyThrows
  public void consume(
      @Payload String message,
      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {

    if (!isJSONValid(message)) {
      log.error("Json message is not valid, please check...");
      throw new IOException("Error is not a valid json iot message");
    }

    log.info("Consuming message...");
    log.info(
        String.format(
            "$$ -> Consumed key [%s] "
                + "Message [%s] "
                + "Partition [%d] "
                + "Topic [%s] "
                + "Timestamp [%d]",
            key, message, partition, topic, ts));

    BasicRequest request =
        new ObjectMapper()
            .readValue(
                new ObjectMapper().readTree(message).get("request").textValue(),
                BasicRequest.class);

    EarthquakeResponse responose =
        new ObjectMapper()
            .readValue(
                new ObjectMapper().readTree(message).get("response").textValue(),
                EarthquakeResponse.class);
    save(request, responose);
  }

  @SneakyThrows
  private void save(BasicRequest request, EarthquakeResponse responses) {
    log.debug("--> saving {}", request.toString());
    repository.save(
        EarthquakeModel.builder()
            .fechaInicio(
                StringUtils.isEmpty(request.getFechaInicio())
                    ? null
                    : getStringToDateFormatComplete(request.getFechaInicio()))
            .fechaFin(
                StringUtils.isEmpty(request.getFechaFin())
                    ? null
                    : getStringToDateFormatComplete(request.getFechaFin()))
            .origen("KAFKA")
            .observacion(
                StringUtils.isEmpty(request.getFechaInicio())
                    ? "[kafka] Buscando magnitudes"
                    : "[kafka] Buscando por fechas y magnitude min")
            .magnitudMin(request.getMagnitudeMin())
            .magnitudMax(request.getMagnitudeMax())
            .salida(new ObjectMapper().writeValueAsString(responses))
            .build());
  }
}
