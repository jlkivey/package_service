package com.clevelanddx.packageintake.config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.microsoft.sqlserver.jdbc.SQLServerAccessTokenCallback;
import com.microsoft.sqlserver.jdbc.SqlAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class AzureSqlDataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    private final AzureTokenProvider tokenProvider;

    public AzureSqlDataSourceConfig(AzureTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL(url);
        
        // Set up a custom connection factory that handles token expiration
        dataSource.setAccessTokenCallback(new SQLServerAccessTokenCallback() {
            @Override
            public SqlAuthenticationToken getAccessToken(String serverName, String port) {
                String token = tokenProvider.getToken();
                return new SqlAuthenticationToken(token, null);
            }
        });
        
        return dataSource;
    }
} 