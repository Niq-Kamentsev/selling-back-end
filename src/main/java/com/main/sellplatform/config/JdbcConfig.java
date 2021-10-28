package com.main.sellplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class JdbcConfig {

    private final DataSource dataSource;
    @Autowired
    public JdbcConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }
}
