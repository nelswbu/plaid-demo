package com.bu.plaid.control;

import com.bu.plaid.model.AccessToken;
import com.bu.plaid.model.InfoResponse;
import com.bu.plaid.model.LinkToken;
import com.bu.plaid.model.PublicToken;
import com.bu.plaid.service.PlaidService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.bu.plaid.control.PlaidController.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class PlaidController {

    public static final String
            REQUEST_MAPPING = "/api",
            CREATE_LINK_TOKEN = "/create_link_token",
            CREATE_PUBLIC_TOKEN = "/create_public_token",
            SET_ACCESS_TOKEN = "/set_access_token",
            EXCHANGE_PUBLIC_TOKEN = "/exchange_public_token";

    @Autowired PlaidService service;

    @PostMapping(CREATE_LINK_TOKEN)
    public ResponseEntity<LinkToken> createLinkToken() {
        return service.getLinkToken();
    }

    @PostMapping(CREATE_PUBLIC_TOKEN)
    public ResponseEntity<PublicToken> createPublicToken() {
        return service.createPublicToken();
    }

    @PostMapping(value = SET_ACCESS_TOKEN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<InfoResponse> setAccessToken(
            @RequestParam(name = "public_token") String publicToken) {
        return service.setAccessToken(publicToken);
    }

    @PostMapping(value = EXCHANGE_PUBLIC_TOKEN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AccessToken> exchangePublicToken(
            @RequestParam(name = "public_token") String publicToken) {
        return service.exchangePublicToken(publicToken);
    }
}
