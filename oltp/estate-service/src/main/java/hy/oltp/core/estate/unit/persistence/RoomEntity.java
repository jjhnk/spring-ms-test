package hy.oltp.core.estate.unit.persistence;

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
public class RoomEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @ManyToOne
  @JoinColumn(name = "unit_id", nullable = false)
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

  public RoomEntity(UnitEntity unit, String name, RoomTypes type, int squareFeet, int windowsCount) {
    this.unit = unit;
    this.name = name;
    this.type = type;
    this.squareFeet = squareFeet;
    this.windowsCount = windowsCount;
  }
}
