package hy.springcloud.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationCustomizer;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation;

@Configuration
public class TracingConfig {
  @Bean
  BaggagePropagation.FactoryBuilder myPropagationFactoryBuilder(
    ObjectProvider<BaggagePropagationCustomizer> baggagePropagationCustomizers) {
    Propagation.Factory delegate = B3Propagation.newFactoryBuilder()
      .injectFormat(B3Propagation.Format.MULTI)
      .build();
    BaggagePropagation.FactoryBuilder builder = BaggagePropagation.newFactoryBuilder(delegate);
    baggagePropagationCustomizers.orderedStream()
      .forEach(customizer -> customizer.customize(builder));
    return builder;
  }
}
