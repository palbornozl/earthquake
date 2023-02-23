package cl.exercise.earthquake.message.producer;

import cl.exercise.earthquake.dto.EarthquakeApiResponse;
import cl.exercise.earthquake.dto.EarthquakeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:kafka-${spring.profiles.active:default}.properties"})
@EnableKafka
@Slf4j
public final class ProducerService {

  @Value(value = "${spring.kafka.template.default-topic}")
  private String topic;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @SneakyThrows
  public void sendMessage(EarthquakeRequest request, EarthquakeApiResponse response) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode result = mapper.createObjectNode();
    result.put("request", mapper.writeValueAsString(request));
    result.put("response", mapper.writeValueAsString(response));
    log.debug(">>>> {}", topic);
    this.kafkaTemplate.send(topic, response.getMagnitude().toString(), result.toPrettyString());
    log.info(String.format("$$ -> Producing message --> %s", result.asText()));
  }
}
