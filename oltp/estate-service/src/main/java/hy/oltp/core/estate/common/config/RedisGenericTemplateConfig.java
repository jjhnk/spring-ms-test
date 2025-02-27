package hy.oltp.core.estate.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisGenericTemplateConfig<T> {

  @Bean
  RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, T> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    var valueSerializer = new GenericJackson2JsonRedisSerializer();
    // TODO: add mixIn valueSerializer.configure(o -> { o.addMixIn() });
    template.setValueSerializer(valueSerializer);
    return template;
  }
}
