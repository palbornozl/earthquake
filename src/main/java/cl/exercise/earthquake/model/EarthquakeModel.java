package cl.exercise.earthquake.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "tbl_earthquake", schema = "earthquake")
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
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  @Type(type = "uuid-char")
  private UUID id;

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

  @Column(name = "token")
  private String token;

  @PrePersist
  protected void prePersist() {
    if (this.createdAt == null) {
      createdAt = Timestamp.from(Instant.now());
      if(this.fechaInicio == null){
        this.fechaInicio = createdAt;
        this.fechaFin = createdAt;
      }
    }
    if(this.magnitudMax == null){
      this.magnitudMax = 0.0f;
    }
  }
}
