package hy.oltp.core.estate.unit.persistence;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UnitDetailEntity {

  @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JsonManagedReference
  @Setter(AccessLevel.NONE)
  private Set<RoomEntity> rooms = new LinkedHashSet<>();

  @Min(0)
  private int totalSquareFeet = 0;

  @NotNull
  @Min(0)
  @Digits(integer = 10, fraction = 2)
  private BigDecimal rent = BigDecimal.ZERO;

  public UnitDetailEntity(
    @Min(0) int totalSquareFeet,
    @NotNull @Min(0) @Digits(integer = 10, fraction = 2) BigDecimal rent) {
    this.totalSquareFeet = totalSquareFeet;
    this.rent = rent;
  }

  public boolean isDefault() {
    return rooms.isEmpty() && totalSquareFeet == 0 && rent.floatValue() == 0;
  }

  public void clear() {
    removeAllRooms();
    totalSquareFeet = 0;
    rent = BigDecimal.ZERO;
  }

  public Set<RoomEntity> getRooms() {
    return rooms;
  }

  public void setRooms(Set<RoomEntity> rooms) {
    for (RoomEntity newRoom : rooms) {
      RoomEntity existingRoom = getRoomById(newRoom.getId());

      if (existingRoom != null) {
        existingRoom.setName(newRoom.getName());
        existingRoom.setType(newRoom.getType());
        existingRoom.setSquareFeet(newRoom.getSquareFeet());
        existingRoom.setWindowsCount(newRoom.getWindowsCount());
      } else {
        this.rooms.add(newRoom);
      }
    }
  }

  public void addRoom(RoomEntity room) {
    if (room == null) {
      return;
    }

    rooms.add(room);
  }

  public void removeRoom(RoomEntity room) {
    if (room == null) {
      return;
    }

    rooms.remove(room);
  }

  public void removeAllRooms() {
    rooms.clear();
  }

  public RoomEntity getRoomById(int roomId) {
    return rooms.stream()
      .filter(x -> x.getId() == roomId)
      .findFirst()
      .orElse(null);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalSquareFeet, rent.floatValue());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    UnitDetailEntity other = (UnitDetailEntity) obj;
    return totalSquareFeet == other.totalSquareFeet && rent.floatValue() == other.rent.floatValue();
  }
}
