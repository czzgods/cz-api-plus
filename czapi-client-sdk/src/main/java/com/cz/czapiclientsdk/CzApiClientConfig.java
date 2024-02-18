package com.cz.czapiclientsdk;

import com.cz.czapiclientsdk.client.CzApiClient;
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

    /**
     * 此处方法取名无所谓的，不影响任何地方
     *
     * @return
     */
    @Bean
    public CzApiClient getApiClient(){
        return new CzApiClient(accessKey,secretKey);
    }
}
