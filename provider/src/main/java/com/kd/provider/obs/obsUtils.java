package com.kd.provider.obs;


import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class obsUtils {

    @Value("${obs.ak}")
    private String ak;

    @Value("${obs.sk}")
    private String sk;

    @Value("${obs.endPoint}")
    private String endPoint;

    @Value("${obs.bucket}")
    private String bucket;


    /**
     * 文件上传
     * @param fileName
     * @param inputStream
     */

    public void upload(String fileName , InputStream inputStream){
        ObsClient obsClient = new ObsClient(ak,sk,endPoint);
        PutObjectResult result = obsClient.putObject(bucket, fileName, inputStream);
        int statusCode = result.getStatusCode();
        if(statusCode != 200){
            throw  new RuntimeException("文件上传异常: 响应代码为" + statusCode);
        }

    }

}
