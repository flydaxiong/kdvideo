package com.kd.provider.controller;


import com.kd.provider.obs.obsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "文件上传")
@RestController()
@RequestMapping("/file")
public class FileController {

    @Autowired
    obsUtils obsUtils;

    @ApiOperation("文件上传")
    @RequestMapping(value = "/upload" , method = RequestMethod.POST)
    public void fileUpload(MultipartFile multipartFile) throws IOException {
        obsUtils.upload(multipartFile.getOriginalFilename(),multipartFile.getInputStream());
    }
}
