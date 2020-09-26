package cl.exercise.earthquake.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "earthquake", schema = "earthquake")
@IdClass(EarthquakeModel.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EarthquakeModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "origen")
  private String origen;

  @Column(name = "observacion")
  private String observacion;

  @Column(name = "fecha_inicio")
  private Timestamp fechaInicio;

  @Column(name = "fecha_fin")
  private Timestamp fechaFin;

  @Column(name = "magnitud_min")
  private Float magnitudMin;

  @Column(name = "magnitud_max")
  private Float magnitudMax;

  @Column(name = "salida")
  private String salida;

  @PrePersist
  protected void prePersist() {
    if (this.createdAt == null) {
      createdAt = Timestamp.from(Instant.now());
    }
  }
}
