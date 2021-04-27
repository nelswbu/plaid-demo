package com.bu.plaid.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@With
@Data
public class InfoResponse {

    String itemId, accessToken;
    List<String> products;

}
