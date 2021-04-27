package com.bu.plaid.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JsonComponent {

    @Autowired ObjectMapper om;

    public String of(Object o) {
        try {
            return om.writeValueAsString(o);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public <T> T to(Class<T> t, String s) {
        try {
            return om.readValue(s, t);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

}
