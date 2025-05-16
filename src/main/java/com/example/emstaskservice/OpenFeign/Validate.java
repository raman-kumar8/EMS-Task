package com.example.emstaskservice.OpenFeign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "validate", url = "${USER_URL}")
public interface Validate {

    @RequestMapping(method = RequestMethod.GET, value = "general/validate")
    String validate(@RequestHeader("Cookie") String cookieHeader);
}
