package com.kd.consumer.config;


import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerCinfig {

    @Bean
    public Docket swaggerConfigs(){
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否开始swagger
                .enable(true)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 暴露接口
                .select()
                // 扫描所有有注解的API,用这种方式比较灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }



    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                .title("obs服务") //设置文档的标题
                .description("obs API 接口文档") // 设置文档的描述
                .version("VERSION-1.0.0") // 设置文档的版本信息-> 1.0.0 Version information
                //  .termsOfServiceUrl("http://www.baidu.com") // 设置文档的License信息->1.3 License information
                .build();
    }
}
