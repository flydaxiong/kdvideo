package com.kd.provider.service.impl;

import com.google.common.collect.Lists;
import com.kd.provider.mapper.ImageMapper;
import com.kd.provider.obs.ObsUtils;
import com.kd.provider.service.ObsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


@Service
public class ObsServiceImpl implements ObsService {

    @Autowired
    ObsUtils obsUtils;

    @Autowired
    ImageMapper imageMapper;


    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<String> imageName() {
        List<String> list = imageMapper.imageUrl();
        return list;
    }


    @Override
    public List<String> imageUrl() {
        List<String> list = Lists.newArrayList();
        imageName().forEach(k -> {
            String image = obsUtils.image(k);
            if(StringUtils.isNotBlank(image)){
                list.add(image);
            }
        });
        redisTemplate.opsForValue().set("img",list);
        return list;
    }


}
