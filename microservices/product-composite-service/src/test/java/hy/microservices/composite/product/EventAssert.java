package hy.microservices.composite.product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.AbstractAssert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import hy.api.event.Event;

public class EventAssert<T, K> extends AbstractAssert<EventAssert<T, K>, Event<T, K>> {
  ObjectMapper mapper = new ObjectMapper();

  protected EventAssert(Event<T, K> actual) {
    super(actual, EventAssert.class);
  }

  public static <T, K> EventAssert<T, K> assertThat(Event<T, K> actual) {
    return new EventAssert<T, K>(actual);
  }

  @SuppressWarnings("rawtypes")
  public EventAssert<T, K> isMatch(String expected) {
    isNotNull();
    Map expectedMapEvent;
    try {
      expectedMapEvent = mapper.readValue(expected, new TypeReference<HashMap>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    expectedMapEvent.remove("eventCreatedAt");

    var actualMapEvent = convertObjectToMap(actual);
    actualMapEvent.remove("eventCreatedAt");

    if (!expectedMapEvent.equals(actualMapEvent)) {
      failWithMessage(expected, "Expected: %s but was: %s", expectedMapEvent, actualMapEvent);
    }

    return this;
  }

  @SuppressWarnings("rawtypes")
  public EventAssert<T, K> isNotMatch(String expected) {
    isNotNull();
    Map expectedMapEvent;
    try {
      expectedMapEvent = mapper.readValue(expected, new TypeReference<HashMap>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    expectedMapEvent.remove("eventCreatedAt");

    var actualMapEvent = convertObjectToMap(actual);
    actualMapEvent.remove("eventCreatedAt");

    if (expectedMapEvent.equals(actualMapEvent)) {
      failWithMessage(expected, "Expected: %s but was: %s", expectedMapEvent, actualMapEvent);
    }

    return this;
  }

  @SuppressWarnings("rawtypes")
  private Map convertObjectToMap(Object object) {
    JsonNode node = mapper.convertValue(object, JsonNode.class);
    return mapper.convertValue(node, Map.class);
  }
}
