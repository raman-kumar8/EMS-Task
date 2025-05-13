package com.example.emstaskservice.OpenFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "validate", url = "http://localhost:8080")
public interface Validate {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/general/validate")
    String validate(@RequestHeader("Cookie") String cookieHeader);
}
