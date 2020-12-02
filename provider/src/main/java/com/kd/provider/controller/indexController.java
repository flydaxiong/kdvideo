package com.kd.provider.controller;

import com.kd.provider.service.ObsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(tags = "图片")
public class indexController {

    @Autowired
    ObsService obsService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/index")
    public String index(Model model){
        List<String> img = (List<String>) redisTemplate.opsForValue().get("img");
        model.addAttribute("image",img);

        return "index";
    }

    @ApiOperation("获取图片信息")
    @ResponseBody
    @RequestMapping(value = "/image" , method = RequestMethod.POST)
    public List<String> getImage(){
        return obsService.imageUrl();
    }
}
