package hy.oltp.core.estate.common.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

@Configuration
@EnableCaching
public class RedisCommonConfig {
  @Value("${spring.data.redis.cluster.nodes:}")
  private List<String> nodes;
  @Value("${spring.data.redis.password:}")
  private String password;
  @Value("${spring.cache.type}")
  private String cacheType;

  @Bean
  RedisConnectionFactory redisConnectionFactory() {
    SocketOptions socketOptions = SocketOptions.builder()
      .connectTimeout(Duration.ofMillis(3000L))
      .keepAlive(true)
      .build();

    ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
      .dynamicRefreshSources(true)
      .enableAllAdaptiveRefreshTriggers()
      .enablePeriodicRefresh(Duration.ofSeconds(30))
      .build();

    ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
      .pingBeforeActivateConnection(true)
      .autoReconnect(true)
      .socketOptions(socketOptions)
      .topologyRefreshOptions(clusterTopologyRefreshOptions)
      .build();

    final LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
      .commandTimeout(Duration.ofMillis(1500L))
      .clientOptions(clusterClientOptions)
      .build();

    RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(nodes);
    clusterConfig.setPassword(password);
    LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
    factory.setValidateConnection(false);

    return factory;
  }

  @Bean
  RedisCacheConfiguration redisCacheConfiguration() {
    var config = RedisCacheConfiguration.defaultCacheConfig();
    config.disableCachingNullValues();
    return config;
  }

  public boolean isRedisEnabled() {
    return cacheType.equals("redis");
  }
}
