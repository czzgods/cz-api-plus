package com.cz.czapiclientsdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *czapi 客户端SDK配置类
 */
@Data
@Configuration
@ConfigurationProperties("czapi.client")
@ComponentScan
public class CzApiClientConfig {
    private String accessKey;
    private String secretKey;
}
