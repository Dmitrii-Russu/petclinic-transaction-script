package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class PostgresConfig {

    @Bean
    EmbeddedPostgres embeddedPostgres() throws IOException {
        return EmbeddedPostgres.builder().setServerConfig("log_min_messages", "panic").start();
    }

    @Bean
    DataSource dataSource(EmbeddedPostgres postgres) {
        return postgres.getPostgresDatabase();
    }
}
