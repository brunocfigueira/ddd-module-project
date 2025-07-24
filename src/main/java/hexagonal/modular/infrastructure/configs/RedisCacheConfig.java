package hexagonal.modular.infrastructure.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CartResponse;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.OrderResponse;
import hexagonal.modular.domain.ecommerce.web.dtos.products.ProductResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    private static final int DEFAULT_TTL_HOURS = 3;
    private static final int PRODUCTS_TTL_HOURS = 1;
    private static final int CARTS_TTL_HOURS = 2;
    private static final int ORDERS_TTL_MINUTES = 25;

    private final ObjectMapper objectMapper;
    private final GenericJackson2JsonRedisSerializer genericSerializer;

    public RedisCacheConfig() {
        this.objectMapper = buildObjectMapper();
        this.genericSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    private ObjectMapper buildObjectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule()) // Suporte a LocalDateTime
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO 8601
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig())
                .withCacheConfiguration("products", productsCacheConfig())
                .withCacheConfiguration("carts", cartsCacheConfig())
                .withCacheConfiguration("orders", ordersCacheConfig())
                .build();
    }

    private RedisCacheConfiguration defaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(DEFAULT_TTL_HOURS))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericSerializer));
    }

    private RedisCacheConfiguration cartsCacheConfig() {
        return buildTypedCacheConfig(CartResponse.class, Duration.ofHours(CARTS_TTL_HOURS));
    }

    private RedisCacheConfiguration ordersCacheConfig() {
        return buildTypedCacheConfig(OrderResponse.class, Duration.ofMinutes(ORDERS_TTL_MINUTES));
    }

    private RedisCacheConfiguration productsCacheConfig() {
        return buildTypedCacheConfig(ProductResponse.class, Duration.ofHours(PRODUCTS_TTL_HOURS));
    }

    private <T> RedisCacheConfiguration buildTypedCacheConfig(Class<T> type, Duration ttl) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, type);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }
}

