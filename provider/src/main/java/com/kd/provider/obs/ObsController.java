package com.kd.provider.obs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Api(tags = "obs")
@RestController
@RequestMapping("/obs")
public class ObsController {

    @Autowired
    ObsUtils obsUtils;

    @ResponseBody
    @ApiOperation("文件上传")
    @RequestMapping(value = "/upload" , method = RequestMethod.POST)
    public void obs(MultipartFile multipartFile) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
      //  obsUtils.uploadFile(inputStream,multipartFile.getOriginalFilename());
        obs.uploadFile(inputStream,multipartFile.getOriginalFilename());
    }



    @ResponseBody
    @ApiOperation("文件下载")
    @RequestMapping(value = "/download" , method = RequestMethod.GET)
    public void download(String fileName, HttpServletResponse response , HttpServletRequest request) throws Exception {
        obsUtils.download(fileName,response,request);

    }

}
