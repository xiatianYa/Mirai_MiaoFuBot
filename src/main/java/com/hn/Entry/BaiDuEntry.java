package com.hn.Entry;

public class BaiDuEntry {
    private final String ApiKey;
    private final String SecretKey;
    private String AccessToken;
    private BaiDuEntry(String apiKey, String secretKey,String accessToken) {
        ApiKey = apiKey;
        SecretKey = secretKey;
        AccessToken= accessToken;
    }
    public static BaiDuBuilder builder(){
        return new BaiDuBuilder();
    }
    public static class BaiDuBuilder{
        String ApiKey;
        String SecretKey;
        String AccessToken;

        public BaiDuBuilder setAccessToken(String accessToken) {
            AccessToken = accessToken;
            return this;
        }

        public BaiDuBuilder setApiKey(String apiKey) {
            ApiKey = apiKey;
            return this;
        }

        public BaiDuBuilder setSecretKey(String secretKey) {
            SecretKey = secretKey;
            return this;
        }
        public BaiDuEntry build(){
            return new BaiDuEntry(ApiKey,SecretKey,AccessToken);
        }
    }

    public String getApiKey() {
        return ApiKey;
    }


    public String getSecretKey() {
        return SecretKey;
    }


    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }
}
