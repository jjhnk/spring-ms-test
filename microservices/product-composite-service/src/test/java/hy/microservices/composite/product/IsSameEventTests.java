package hy.microservices.composite.product;

import static hy.api.event.Event.Type.CREATE;
import static hy.api.event.Event.Type.DELETE;
import static hy.microservices.composite.product.EventAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hy.api.core.product.Product;
import hy.api.event.Event;

class IsSameEventTests {
  ObjectMapper mapper = new ObjectMapper();

  @Test
  void testEventObjectCompare() throws JsonProcessingException {
    Event<Integer, Product> event1 = new Event<>(CREATE, 1, new Product(1, "name", 1, null));
    Event<Integer, Product> event2 = new Event<>(CREATE, 1, new Product(1, "name", 1, null));
    Event<Integer, Product> event3 = new Event<>(DELETE, 1, null);
    Event<Integer, Product> event4 = new Event<>(CREATE, 1, new Product(2, "name", 1, null));

    String event1Json = mapper.writeValueAsString(event1);

    assertThat(event1).isMatch(event1Json);
    assertThat(event2).isMatch(event1Json);
    assertThat(event3).isNotMatch(event1Json);
    assertThat(event4).isNotMatch(event1Json);
  }

}
