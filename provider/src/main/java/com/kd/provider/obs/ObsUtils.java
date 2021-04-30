package com.kd.provider.obs;


import com.obs.services.ObsClient;
import com.obs.services.model.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class ObsUtils {




    private static ObsConfigProperties configProperties;

    private static ObsClient obsClient;

    public ObsUtils() {}

    private static final Logger log= LoggerFactory.getLogger(ObsUtils.class);

    @Autowired
    public void setProperties(ObsConfigProperties obsConfigProperties) {
        configProperties = obsConfigProperties;
    }

    @PostConstruct
    private void init(){
        obsClient = new ObsClient(configProperties.getAk(),configProperties.getSk(),configProperties.getEndPoint());
    }

    /**
     * 通过输入流的方式上传文件
     * @param
     * @param inputStream
     * @return
     */
    public void uploadFile(InputStream inputStream,String fileName) {
        PutObjectResult putObjectResult = obsClient.putObject(configProperties.getBucket(), fileName, inputStream);
        int statusCode = putObjectResult.getStatusCode();
        String objectUrl = putObjectResult.getObjectUrl();
        System.out.println(objectUrl);
        if (statusCode != 200) {
            throw new RuntimeException("文件上传异常，http响应码为" + statusCode);
        }
    }



    /**
     * 文件下载
     * @param fileName
     */
    public void download(String fileName , HttpServletResponse response , HttpServletRequest request) throws IOException {
        ObsObject object = obsClient.getObject(configProperties.getBucket(), fileName);
        // 读取对象内容
        InputStream inputStream = object.getObjectContent();
        byte[] b = new byte[1024];
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        // 防止文件名出现乱码
        final String userAgent = request.getHeader("USER-AGENT");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"UTF-8"));
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }


    /**
     * 图像处理
     * @param imageName
     */
    public String image(String imageName){
        long expireSeconds = 3600L;
        // 创建ObsClient实例
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        try {
            ObsObject object = obsClient.getObject(configProperties.getBucket(), imageName);
            String objectKey = object.getObjectKey();
            System.out.println(objectKey);
        }catch (Exception e){
            log.error("图片不存在"+imageName);
            return "";
        }
        request.setBucketName(configProperties.getBucket());
        request.setObjectKey(imageName);
        // 设置图片处理参数，对图片依次进行缩放、旋转
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("x-image-process", "image/resize,m_fixed,w_100,h_100/rotate,90");
        request.setQueryParams(queryParams);
        // 生成临时授权URL
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        return response.getSignedUrl();
    }


}

