package hy.microservices.core.product.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import hy.api.core.product.Product;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.cluster.nodes}")
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
    RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Product> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    ValueOperations<String, Product> valueOps(RedisTemplate<String, Product> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    public boolean isRedisEnabled() {
        return cacheType.equals("redis");
    }
}