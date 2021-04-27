package com.bu.plaid.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@With
@Data
public class PublicToken {

    String publicToken, requestId;

}
