package com.kd.consumer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "obs服务")
@RestController
@RequestMapping("/obs")
public class Controller {

    @ApiOperation("obs服务")
    @RequestMapping(value = "/obs",method = RequestMethod.GET)
    public String obs(String name){
        return name;
    }
}
