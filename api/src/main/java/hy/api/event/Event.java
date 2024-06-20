package hy.api.event;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

public class Event<K, T> {
  public enum Type {
    CREATE, DELETE
  }

  private final Type type;
  private final K key;
  private final T data;
  private final ZonedDateTime eventCreatedAt;

  public Event() {
    this(null, null, null);
  }

  public Event(Type type, K key, T data) {
    this.type = type;
    this.key = key;
    this.data = data;
    this.eventCreatedAt = now();
  }

  public Type getType() {
    return type;
  }

  public K getKey() {
    return key;
  }

  public T getData() {
    return data;
  }

  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  public ZonedDateTime getEventCreatedAt() {
    return eventCreatedAt;
  }
}
