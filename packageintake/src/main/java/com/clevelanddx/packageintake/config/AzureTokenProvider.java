package com.clevelanddx.packageintake.config;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AzureTokenProvider {
    private final String tenantId;
    private final String clientId;
    private final String clientSecret;
    private final AtomicReference<String> tokenCache = new AtomicReference<>();
    private final AtomicReference<Instant> tokenExpiry = new AtomicReference<>();

    public AzureTokenProvider(
            @Value("${azure.tenant-id}") String tenantId,
            @Value("${azure.client-id}") String clientId,
            @Value("${azure.client-secret}") String clientSecret) {
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getToken() {
        Instant now = Instant.now();
        Instant expiry = tokenExpiry.get();

        // If token is null or expired (with 5-minute buffer), get a new one
        if (tokenCache.get() == null || expiry == null || now.plusSeconds(300).isAfter(expiry)) {
            synchronized (this) {
                // Double-check after acquiring lock
                if (tokenCache.get() == null || expiry == null || now.plusSeconds(300).isAfter(expiry)) {
                    refreshToken();
                }
            }
        }

        String token = tokenCache.get();
        if (token == null) {
            throw new RuntimeException("Failed to obtain Azure AD token");
        }
        return token;
    }

    private void refreshToken() {
        try {
            ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                    .tenantId(tenantId)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();

            // Create token request context for Azure SQL Database
            TokenRequestContext requestContext = new TokenRequestContext()
                    .addScopes("https://database.windows.net/.default");

            // Get token and block until it's available
            String token = credential.getToken(requestContext)
                    .block()
                    .getToken();
            
            // Cache the token
            tokenCache.set(token);
            
            // Set expiry time (typically 1 hour from now)
            tokenExpiry.set(Instant.now().plusSeconds(3600));
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh Azure AD token", e);
        }
    }
} 