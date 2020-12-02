package com.kd.provider.obs;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/30 22:38
 */

//@Component
//@ConfigurationProperties(prefix = "obs")
public class ObsConfigProperties {
    @Value("${obs.endPoint}")
    private String endPoint ;

    @Value("${obs.ak}")
    private String ak;

    @Value("${obs.sk}")
    private String sk;

    @Value("${obs.bucket}")
    private String bucket;
}
