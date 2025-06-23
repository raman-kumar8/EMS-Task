package com.example.emstaskservice.openfeign;


import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "validate", url = "${USER_URL}")
public interface Validate {


    @GetMapping(path = "general/validate")
    String validate(@RequestHeader("Cookie") String cookieHeader);
}
