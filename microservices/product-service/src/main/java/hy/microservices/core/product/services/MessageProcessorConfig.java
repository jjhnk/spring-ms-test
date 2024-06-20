package hy.microservices.core.product.services;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.event.Event;
import hy.api.exceptions.EventProcessingException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessageProcessorConfig {
  private final ProductService productService;

  public MessageProcessorConfig(ProductService productService) {
    this.productService = productService;
  }

  @Bean
  EventConsumer messageProcessor() {
    return new EventConsumer(productService);
  }

  public class EventConsumer implements Consumer<Event<Integer, Product>> {
    private final ProductService productService;
    private final String className = getClass().getSimpleName();

    public EventConsumer(ProductService productService) {
      this.productService = productService;
    }

    @Override
    public void accept(Event<Integer, Product> event) {
      log.info("{} received event: {}", className, event);

      switch (event.getType()) {
        case CREATE:
          Product product = event.getData();
          log.info("{} create product: {}", className, product);
          productService.createProduct(product)
            .block();
          break;
        case DELETE:
          int productId = event.getKey();
          log.info("{} delete productId: {}", className, productId);
          productService.deleteProduct(productId)
            .block();
          break;
        default:
          String errorMessage = "Incorrect event type: " + event.getType() + ", expected a CREATE or DELETE event";
          log.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }

      log.info("{} processed event: {}", className, event);
    }
  }
}
