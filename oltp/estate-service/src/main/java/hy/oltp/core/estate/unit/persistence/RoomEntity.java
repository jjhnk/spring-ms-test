package hy.oltp.core.estate.unit.persistence;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.api.core.estate.unit.RoomTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RoomEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @ManyToOne
  @JoinColumn(name = "unit_id", nullable = false)
  // @JsonBackReference
  private UnitEntity unit;

  @NotNull
  private String name;

  @Enumerated(EnumType.STRING)
  @NotNull
  private RoomTypes type;

  @Min(0)
  private int squareFeet;

  @Min(0)
  private int windowsCount;

  public RoomEntity(String name, RoomTypes type, int squareFeet, int windowsCount) {
    this.name = name;
    this.type = type;
    this.squareFeet = squareFeet;
    this.windowsCount = windowsCount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, squareFeet, windowsCount);
  }

  // @formatter:off
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    RoomEntity other = (RoomEntity) obj;
    return name.equals(other.name)
      && type.equals(other.type)
      && squareFeet == other.squareFeet
      && windowsCount == other.windowsCount;
  }
  // @formatter:on
}
