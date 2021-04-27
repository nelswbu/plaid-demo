package com.bu.plaid;

import com.bu.plaid.component.JsonComponent;
import com.bu.plaid.model.AccessToken;
import com.bu.plaid.model.InfoResponse;
import com.bu.plaid.model.LinkToken;
import com.bu.plaid.model.PublicToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.bu.plaid.control.PlaidController.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Log4j2
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class AppTests {

    static final Map<String, String> tokens = new HashMap<>();

    @Autowired MockMvc mvc;
    @Autowired JsonComponent json;

    @Test
    @Order(10)
    void test_create_link_token() throws Exception {

        String s = mvc.perform(
                post(REQUEST_MAPPING + CREATE_LINK_TOKEN))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LinkToken linkToken = json.to(LinkToken.class, s);

        log.info("linkToken: [{}]", json.of(linkToken));

        tokens.put("linkToken", linkToken.getLinkToken());
    }

    @Test
    @Order(15)
    void test_create_public_token() throws Exception {

        String s = mvc.perform(
                post(REQUEST_MAPPING + CREATE_PUBLIC_TOKEN))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PublicToken publicToken = json.to(PublicToken.class, s);

        log.info("publicToken: [{}]", json.of(publicToken));

        tokens.put("publicToken", publicToken.getPublicToken());
    }

    @Test
    @Order(20)
    void test_set_access_token() throws Exception {

        String s = mvc.perform(
                post(REQUEST_MAPPING + SET_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("public_token", tokens.get("publicToken")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        InfoResponse infoResponse = json.to(InfoResponse.class, s);

        log.info("infoResponse: [{}]", json.of(infoResponse));

        tokens.put("accessToken", infoResponse.getAccessToken());

    }

    @Test
    @Order(30)
    void test_exchange_public_token() throws Exception {

        String s = mvc.perform(
                post(REQUEST_MAPPING + EXCHANGE_PUBLIC_TOKEN)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("public_token", tokens.get("publicToken")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccessToken accessToken = json.to(AccessToken.class, s);

        log.info("accessToken: [{}]", json.of(accessToken));

        tokens.put("accessToken", accessToken.getAccessToken());

    }

}
