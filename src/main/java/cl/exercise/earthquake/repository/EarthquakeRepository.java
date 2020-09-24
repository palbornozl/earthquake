package cl.exercise.earthquake.repository;

import cl.exercise.earthquake.model.EarthquakeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EarthquakeRepository extends JpaRepository<EarthquakeModel, Long> {}
