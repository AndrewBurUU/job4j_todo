package ru.job4j.todo.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfiguration {

    @Bean
    public DataSource connectionPool(@Value("${datasource.url}") String url,
                                     @Value("${datasource.username}") String username,
                                     @Value("${datasource.password}") String password) {
        return new BasicDataSource() {
            {
                setUrl(url);
                setUsername(username);
                setPassword(password);
            }
        };
    }
}