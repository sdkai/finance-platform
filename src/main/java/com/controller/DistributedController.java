package com.controller;

import com.service.DistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/distributed/lock")
public class DistributedController {

    @Autowired
    private DistributedService distributedService;

    @RequestMapping(value = "/getLock", method = RequestMethod.GET)
    public String getLock(String key) {

        return distributedService.getLock(key);
    }
}
