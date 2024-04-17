package com.github.simple_mocks.local_mocks.conf;

import com.github.simple_mocks.error.local.EnableLocalErrorService;
import com.github.simple_mocks.local_mocks.Application;
import com.github.simple_mocks.session.local.EnableLocalSessionService;
import com.github.simple_mocks.storage.local.EnableLocalStorageService;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@EnableLocalStorageService
@EnableLocalSessionService
@EnableLocalErrorService("/config/mocks/errors")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = {Application.class}
)
public class LocalConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
            EntityManagerFactoryBuilder managerFactoryBuilder) {
        return managerFactoryBuilder
                .dataSource(dataSource)
                .packages(Application.class)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean managerFactoryBean) {
        var entityManagerFactory = managerFactoryBean.getObject();
        Objects.requireNonNull(entityManagerFactory);
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.flyway")
    public ClassicConfiguration flywayConfiguration(DataSource dataSource) {
        var classicConfiguration = new ClassicConfiguration();
        classicConfiguration.setDataSource(dataSource);
        return classicConfiguration;
    }

    @Bean
    @Primary
    public Flyway flyway(ClassicConfiguration configuration) {
        var flyway = new Flyway(configuration);
        flyway.migrate();
        return flyway;
    }

}
