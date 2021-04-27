package com.bu.plaid.service;

import com.bu.plaid.model.AccessToken;
import com.bu.plaid.model.InfoResponse;
import com.bu.plaid.model.LinkToken;
import com.bu.plaid.model.PublicToken;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.LinkTokenCreateRequest;
import com.plaid.client.request.SandboxPublicTokenCreateRequest;
import com.plaid.client.request.common.Product;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.LinkTokenCreateResponse;
import com.plaid.client.response.SandboxPublicTokenCreateResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Log4j2
@Service
public class PlaidService {

    static final String
            getLinkToken = "get link token",
            setAccessToken = "set access token",
            createPublicToken = "create public token",
            exchangePublicToken = "exchange public token";

    @Value("#{'${plaid_country_codes}'.split(',')}") String[] countryCodes;
    @Value("#{'${plaid_products}'.split(',')}") String[] plaidProducts;
    @Value("${plaid_institution_id}") String institutionId;
    @Value("${spring.application.name}") String appName;

    @Autowired PlaidClient plaidClient;

    private List<String> products() {
        return Arrays.asList(plaidProducts);
    }

    private List<String> countries() {
        return Arrays.asList(countryCodes);
    }

    public ResponseEntity<LinkToken> getLinkToken() {

        try {

            log.info("{} [{}]", getLinkToken, "???");

            Response<LinkTokenCreateResponse> res = plaidClient
                    .service()
                    .linkTokenCreate(new LinkTokenCreateRequest(
                            new LinkTokenCreateRequest.User("user-id"),
                            appName,
                            products(),
                            countries(),
                            "en"))
                    .execute();

            if (res.code() != 200) {
                log.warn(res.message());
                log.warn(res.errorBody().string());
                return ResponseEntity.status(res.code()).build();
            }

            log.info("{} [{}]", getLinkToken, 200);
            return ResponseEntity.ok(new LinkToken(res.body().getLinkToken()));

        } catch (Exception e) {
            log.error(e);
            log.error("{} [{}]", getLinkToken, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<InfoResponse> setAccessToken(String publicToken) {

        try {

            log.info("{} [{}]", setAccessToken, "???");

            Response<ItemPublicTokenExchangeResponse> res = plaidClient
                    .service()
                    .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken))
                    .execute();

            if (res.code() != 200) {
                log.warn(res.message());
                log.warn(res.errorBody().string());
                log.warn("{} [{}]", setAccessToken, res.code());
                return ResponseEntity.status(res.code()).build();
            }

            log.info("{} [{}]", setAccessToken, 200);

            return ResponseEntity.ok(new InfoResponse()
                    .withProducts(products())
                    .withAccessToken(res.body().getAccessToken())
                    .withItemId(res.body().getItemId()));

        } catch (Exception e) {
            log.error(e);
            log.error("{} [{}]", setAccessToken, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<PublicToken> createPublicToken() {

        try {

            log.info("{} [{}]", createPublicToken, "???");

            Response<SandboxPublicTokenCreateResponse> res = plaidClient
                    .service()
                    .sandboxPublicTokenCreate(
                            new SandboxPublicTokenCreateRequest(institutionId, List.of(Product.TRANSACTIONS)))
                    .execute();

            if (res.code() != 200) {
                log.warn(res.message());
                log.warn(res.errorBody().string());
                log.warn("{} [{}]", createPublicToken, res.code());
                return ResponseEntity.status(res.code()).build();
            }

            log.info("{} [{}]", createPublicToken, 200);

            var result = new PublicToken()
                    .withPublicToken(res.body().getPublicToken())
                    .withRequestId(res.body().getRequestId());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error(e);
            log.error("{} [{}]", createPublicToken, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<AccessToken> exchangePublicToken(String publicToken) {

        try {

            log.info("{} [{}]", exchangePublicToken, "???");

            Response<ItemPublicTokenExchangeResponse> res = plaidClient
                    .service()
                    .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken))
                    .execute();

            if (res.code() != 200) {
                log.warn(res.message());
                log.warn(res.errorBody().string());
                log.warn("{} [{}]", exchangePublicToken, res.code());
                return ResponseEntity.status(res.code()).build();
            }

            log.info("{} [{}]", exchangePublicToken, 200);

            var result = new AccessToken()
                    .withAccessToken(res.body().getAccessToken())
                    .withRequestId(res.body().getRequestId())
                    .withItemId(res.body().getItemId());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error(e);
            log.error("{} [{}]", exchangePublicToken, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
