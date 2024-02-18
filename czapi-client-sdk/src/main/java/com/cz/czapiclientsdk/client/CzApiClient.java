package com.cz.czapiclientsdk.client;

/**
 * API调用
 */
public class CzApiClient {
    //网关地址
    public static String GATEWAY_HOST = "http://localhost:8090";
    private String accessKey;
    private String secretKey;

    /**
     * 构造方法
     * @param accessKey
     * @param secretKey
     */
    public CzApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 设置网关地址
     * @param gatewayHost
     */
    public void setGatewayHost(String gatewayHost){
        GATEWAY_HOST = gatewayHost;
    }

}
