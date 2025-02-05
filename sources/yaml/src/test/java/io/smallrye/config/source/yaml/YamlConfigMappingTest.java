package io.smallrye.config.source.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

class YamlConfigMappingTest {
    @Test
    void yamlConfigMapping() throws Exception {
        SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withMapping(Proxies.class, "proxies")
                .withSources(new YamlConfigSource(YamlConfigSourceTest.class.getResource("/example-mapping.yml")))
                .build();

        Proxies proxies = config.getConfigMapping(Proxies.class);

        assertFalse(proxies.allProxies().isEmpty());
        assertEquals(SQLProxyConfig.DatabaseType.mssql, proxies.allProxies().get(0).type());
        assertEquals("mssql", proxies.allProxies().get(0).typeAsString());
        assertEquals("first", proxies.allProxies().get(0).name());
        assertEquals(-1, proxies.allProxies().get(0).maxAsyncThreads());
        assertEquals(-1, proxies.allProxies().get(0).maxQueuedThreads());

        assertTrue(proxies.allProxies().get(0).input().pool().isPresent());
        assertEquals(60, proxies.allProxies().get(0).input().pool().get().expiresInSeconds());
        assertTrue(proxies.allProxies().get(0).input().pool().get().maxPoolSize().isPresent());
        assertEquals(100, proxies.allProxies().get(0).input().pool().get().maxPoolSize().getAsInt());
        assertTrue(proxies.allProxies().get(0).input().mssql().isPresent());
        assertEquals("192.168.1.2", proxies.allProxies().get(0).input().mssql().get().server());
        assertEquals(1434, proxies.allProxies().get(0).input().mssql().get().port());
        assertEquals("test", proxies.allProxies().get(0).input().mssql().get().user());
        assertEquals("test", proxies.allProxies().get(0).input().mssql().get().password());
        assertFalse(proxies.allProxies().get(0).input().mssql().get().hostName().isPresent());
        assertFalse(proxies.allProxies().get(0).input().mssql().get().database().isPresent());
        assertFalse(proxies.allProxies().get(0).input().mssql().get().timeout().isPresent());
        assertFalse(proxies.allProxies().get(0).input().regex().isPresent());

        assertTrue(proxies.allProxies().get(0).read().pool().isPresent());
        assertEquals(60, proxies.allProxies().get(0).read().pool().get().expiresInSeconds());
        assertTrue(proxies.allProxies().get(0).read().pool().get().maxPoolSize().isPresent());
        assertEquals(100, proxies.allProxies().get(0).read().pool().get().maxPoolSize().getAsInt());
        assertTrue(proxies.allProxies().get(0).read().mssql().isPresent());
        assertEquals("127.0.0.1", proxies.allProxies().get(0).read().mssql().get().server());
        assertEquals(1433, proxies.allProxies().get(0).read().mssql().get().port());
        assertEquals("sa", proxies.allProxies().get(0).read().mssql().get().user());
        assertEquals("Test!1234", proxies.allProxies().get(0).read().mssql().get().password());
        assertFalse(proxies.allProxies().get(0).read().mssql().get().hostName().isPresent());
        assertFalse(proxies.allProxies().get(0).read().mssql().get().database().isPresent());
        assertFalse(proxies.allProxies().get(0).read().mssql().get().timeout().isPresent());
        assertTrue(proxies.allProxies().get(0).read().regex().isPresent());
        assertEquals("(?i)^\\s*select.*", proxies.allProxies().get(0).read().regex().get());

        assertTrue(proxies.allProxies().get(0).write().pool().isPresent());
        assertEquals(60, proxies.allProxies().get(0).write().pool().get().expiresInSeconds());
        assertTrue(proxies.allProxies().get(0).write().pool().get().maxPoolSize().isPresent());
        assertEquals(100, proxies.allProxies().get(0).write().pool().get().maxPoolSize().getAsInt());
        assertTrue(proxies.allProxies().get(0).write().mssql().isPresent());
        assertEquals("127.0.0.1", proxies.allProxies().get(0).write().mssql().get().server());
        assertEquals(1433, proxies.allProxies().get(0).write().mssql().get().port());
        assertEquals("sa", proxies.allProxies().get(0).write().mssql().get().user());
        assertEquals("Test!1234", proxies.allProxies().get(0).write().mssql().get().password());
        assertFalse(proxies.allProxies().get(0).write().mssql().get().hostName().isPresent());
        assertFalse(proxies.allProxies().get(0).write().mssql().get().database().isPresent());
        assertFalse(proxies.allProxies().get(0).write().mssql().get().timeout().isPresent());
        assertFalse(proxies.allProxies().get(0).write().regex().isPresent());

        assertEquals(SQLProxyConfig.DatabaseType.mssql, proxies.allProxies().get(1).type());
        assertEquals("mssql", proxies.allProxies().get(1).typeAsString());
        assertEquals("second", proxies.allProxies().get(1).name());
        assertEquals(-1, proxies.allProxies().get(1).maxAsyncThreads());
        assertEquals(-1, proxies.allProxies().get(1).maxQueuedThreads());

        assertTrue(proxies.allProxies().get(1).input().pool().isPresent());
        assertEquals(0, proxies.allProxies().get(1).input().pool().get().expiresInSeconds());
        assertTrue(proxies.allProxies().get(1).input().pool().get().maxPoolSize().isPresent());
        assertEquals(0, proxies.allProxies().get(1).input().pool().get().maxPoolSize().getAsInt());

        assertTrue(proxies.allProxies().get(1).input().encryption().isPresent());
        assertEquals("SUPPORTED", proxies.allProxies().get(1).input().encryption().get().levelAsString());
        assertEquals("TLS", proxies.allProxies().get(1).input().encryption().get().sslProtocol());
        assertTrue(proxies.allProxies().get(1).input().encryption().get().keystore().isPresent());
        assertEquals("my-location", proxies.allProxies().get(1).input().encryption().get().keystore().get().location());
        assertEquals("PCKS12", proxies.allProxies().get(1).input().encryption().get().keystore().get().format());
    }

    @ConfigMapping(prefix = "proxies")
    public interface Proxies {
        @WithParentName
        List<SQLProxyConfig> allProxies();
    }

    public interface SQLProxyConfig {
        @WithName("name")
        String name();

        enum DatabaseType {
            mssql,
            oracle,
            plsql;
        }

        @WithName("type")
        @WithDefault("mssql")
        String typeAsString();

        default DatabaseType type() {
            return DatabaseType.valueOf(typeAsString());
        }

        @WithName("max_async_threads")
        @WithDefault("-1")
        int maxAsyncThreads();

        @WithName("max_queued_threads")
        @WithDefault("-1")
        int maxQueuedThreads();

        @WithName("input")
        ConnectionConfig input();

        @WithName("read")
        ConnectionConfig read();

        @WithName("write")
        ConnectionConfig write();
    }

    interface ConnectionConfig {
        interface PoolConfig {
            String DEFAULT_EXPIRE_IN_SECONDS = "600";

            @WithName("max_pool_size")
            OptionalInt maxPoolSize();

            @WithName("expires_in_seconds")
            @WithDefault(DEFAULT_EXPIRE_IN_SECONDS)
            int expiresInSeconds();
        }

        interface MSSQLConfig {
            String DEFAULT_PORT = "1433";

            @ConfigMapping(prefix = "timeout")
            interface TimeoutConfig {
                String DEFAULT_LOGIN_TIMEOUT = "15";
                String DEFAULT_QUERY_TIMEOUT = "600";

                @WithName("pre_login_in_seconds")
                @WithDefault(DEFAULT_LOGIN_TIMEOUT)
                int preLogin();

                @WithName("login_in_seconds")
                @WithDefault(DEFAULT_LOGIN_TIMEOUT)
                int login();

                @WithName("query_in_seconds")
                @WithDefault(DEFAULT_QUERY_TIMEOUT)
                int query();
            }

            @WithName("database")
            Optional<String> database();

            @WithName("host_name")
            Optional<String> hostName();

            @WithName("server")
            String server();

            @WithName("port")
            @WithDefault(DEFAULT_PORT)
            int port();

            @WithName("user")
            String user();

            @WithName("password")
            String password();

            @WithName("timeout")
            Optional<TimeoutConfig> timeout();
        }

        interface EncryptionConfig {
            @WithName("level")
            @WithDefault("NOT_SUPPORTED")
            String levelAsString();

            @WithName("ssl_protocol")
            @WithDefault("TLS")
            String sslProtocol();

            interface KeystoreConfig {
                @WithName("location")
                String location();

                @WithName("format")
                @WithDefault("PCKS12")
                String format();

                @WithName("password")
                Optional<String> password();
            }

            @WithName("keystore")
            Optional<EncryptionConfig.KeystoreConfig> keystore();

            @WithName("truststore")
            Optional<EncryptionConfig.KeystoreConfig> truststore();
        }

        @WithName("encryption")
        Optional<EncryptionConfig> encryption();

        @WithName("mssql")
        Optional<MSSQLConfig> mssql();

        @WithName("pool")
        Optional<PoolConfig> pool();

        @WithName("regex")
        Optional<String> regex();
    }

    @Test
    void yamlListMaps() {
        SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withMapping(MapConfig.class, "app")
                .withSources(new YamlConfigSource("yaml", "app:\n" +
                        "  config:\n" +
                        "    - name: Bob\n" +
                        "      foo: thing\n" +
                        "      bar: false\n" +
                        "    - name: Tim\n" +
                        "      baz: stuff\n" +
                        "      qux: 3"))
                .build();

        MapConfig mapping = config.getConfigMapping(MapConfig.class);
        assertEquals("Bob", mapping.config().get(0).get("name"));
        assertEquals("thing", mapping.config().get(0).get("foo"));
        assertEquals("false", mapping.config().get(0).get("bar"));
        assertEquals("Tim", mapping.config().get(1).get("name"));
        assertEquals("stuff", mapping.config().get(1).get("baz"));
        assertEquals("3", mapping.config().get(1).get("qux"));
    }

    @ConfigMapping(prefix = "app")
    interface MapConfig {
        List<Map<String, String>> config();
    }
}
