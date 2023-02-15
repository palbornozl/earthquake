package cl.exercise.earthquake.repository;

import cl.exercise.earthquake.model.EarthquakeModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EarthquakeRepository extends CrudRepository<EarthquakeModel, UUID> {
  List<EarthquakeModel> findByOrigen(@Param("origen") String origen);
}
