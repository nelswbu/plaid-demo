package com.bu.plaid.config;

import com.plaid.client.PlaidClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaidConfig {

    @Value("${plaid_client_id}") String clientId;
    @Value("${plaid_secret}") String secret;
    @Value("${plaid_env}") String env;

    @Bean
    public PlaidClient plaidClient() {
        PlaidClient.Builder builder = PlaidClient
                .newBuilder()
                .clientIdAndSecret(clientId, secret);
        switch (env) {
            case "sandbox":
                builder = builder.sandboxBaseUrl();
                break;
            case "development":
                builder = builder.developmentBaseUrl();
                break;
            case "production":
                builder = builder.productionBaseUrl();
                break;
            default:
                throw new IllegalArgumentException("unknown environment: " + env);
        }
        return builder.build();
    }

}
