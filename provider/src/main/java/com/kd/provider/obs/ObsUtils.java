package com.kd.provider.obs;


import com.obs.services.ObsClient;
import com.obs.services.model.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${obs.endPoint}")
    private String endPoint;

    @Value("${obs.ak}")
    private String ak;

    @Value("${obs.sk}")
    private String sk;

    @Value("${obs.bucket}")
    private String bucket;




    private static final Logger log= LoggerFactory.getLogger(ObsUtils.class);

    /**
     * 通过输入流的方式上传文件
     * @param
     * @param inputStream
     * @return
     */
    public void uploadFile(InputStream inputStream,String fileName) {
        ObsClient obsClient = new ObsClient(ak,sk,endPoint);
        PutObjectResult putObjectResult = obsClient.putObject(bucket, fileName, inputStream);
        int statusCode = putObjectResult.getStatusCode();
        String objectUrl = putObjectResult.getObjectUrl();
        System.out.println(objectUrl);
        if (statusCode != 200) {
            throw new RuntimeException("文件上传异常，http响应码为" + statusCode);
        }
    }

    /**
     * 获取文件上传的速率
     * @param
     * @param fileName
     */
    public  void uploadSpeed(MultipartFile multipartFile, String fileName){
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        PutObjectRequest request = new PutObjectRequest(bucket, fileName);
        request.setFile(new File(fileName));
        request.setProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus status) {
                // 上传的平均速率
                System.out.println("平均速率: "+ status.getAverageSpeed());
                // 获取上传进度的百分比
                System.out.println("百分比: "+ status.getTransferPercentage());
            }
        });
        // 每上传1K数据反馈上传进度
        request.setProgressInterval(1024);
        obsClient.putObject(request);
    }

    /**
     * 文件下载
     * @param fileName
     */
    public void download(String fileName , HttpServletResponse response , HttpServletRequest request) throws IOException {
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        ObsObject object = obsClient.getObject(bucket, fileName);
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
        final ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        try {
            ObsObject object = obsClient.getObject(bucket, imageName);
            String objectKey = object.getObjectKey();
            System.out.println(objectKey);
        }catch (Exception e){
            log.error("图片不存在"+imageName);
            return "";
        }
        request.setBucketName(bucket);
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

