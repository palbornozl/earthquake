package cl.exercise.earthquake.message.consumer;

import static cl.exercise.earthquake.utils.Utils.isJSONValid;

import cl.exercise.earthquake.dto.EarthquakeApiResponse;
import cl.exercise.earthquake.dto.EarthquakeRequest;
import cl.exercise.earthquake.repository.EarthquakeRepository;
import cl.exercise.earthquake.transformer.ResponseTransformer;
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

@Service
@PropertySource({"classpath:kafka-${spring.profiles.active:default}.properties"})
@EnableKafka
@Slf4j
public final class ConsumerService {
  final EarthquakeRepository repository;
  final ResponseTransformer transformer;

  public ConsumerService(EarthquakeRepository repository, ResponseTransformer transformer) {
    this.repository = repository;
    this.transformer = transformer;
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

    EarthquakeRequest request =
        new ObjectMapper()
            .readValue(
                new ObjectMapper().readTree(message).get("request").textValue(),
                EarthquakeRequest.class);

    EarthquakeApiResponse responose =
        new ObjectMapper()
            .readValue(
                new ObjectMapper().readTree(message).get("response").textValue(),
                EarthquakeApiResponse.class);
    save(request, responose);
  }

  @SneakyThrows
  private void save(EarthquakeRequest request, EarthquakeApiResponse responses) {
    log.debug("--> saving {}", request.toString());
    repository.save(transformer.transformRequestToEarthquakeModel(request,responses, "KAFKA"));
  }
}
