package com.clevelanddx.packageintake.config;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class AzureSQLTokenProvider {
    private final ClientSecretCredential credential;
    private final AtomicReference<String> cachedToken = new AtomicReference<>();
    private final AtomicReference<Instant> tokenExpiry = new AtomicReference<>();

    public AzureSQLTokenProvider(
            @Value("${azure.tenant-id}") String tenantId,
            @Value("${azure.client-id}") String clientId,
            @Value("${azure.client-secret}") String clientSecret) {
        this.credential = new ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    public String getAccessToken() {
        if (isTokenValid()) {
            return cachedToken.get();
        }
        return refreshToken();
    }

    private boolean isTokenValid() {
        String token = cachedToken.get();
        Instant expiry = tokenExpiry.get();
        return token != null && expiry != null && Instant.now().isBefore(expiry);
    }

    private synchronized String refreshToken() {
        if (isTokenValid()) {
            return cachedToken.get();
        }

        try {
            TokenRequestContext context = new TokenRequestContext();
            context.addScopes("https://database.windows.net/.default");
            
            String token = credential.getToken(context).block().getToken();
            cachedToken.set(token);
            // Set token expiry to 55 minutes from now (tokens are valid for 1 hour)
            tokenExpiry.set(Instant.now().plusSeconds(55 * 60));
            return token;
        } catch (Exception e) {
            log.error("Failed to refresh Azure SQL token", e);
            throw new RuntimeException("Failed to refresh Azure SQL token", e);
        }
    }
} 