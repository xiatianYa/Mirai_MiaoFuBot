package com.hncpu.maomao.Entry;

public class KdiEntry {
    private final String username;
    private final String password;
    private final String DpsPath;
    private final String SignaturePath;
    private final String isSignature;
    private final String SecretId;
    private final String SecretKey;
    private final String Signature;

    private KdiEntry(String username, String password, String dpsPath, String signaturePath, String isSignature, String secretId, String secretKey, String signature) {
        this.username = username;
        this.password = password;
        this.DpsPath = dpsPath;
        this.SignaturePath = signaturePath;
        this.isSignature = isSignature;
        this.SecretId = secretId;
        this.SecretKey = secretKey;
        this.Signature = signature;
    }

    public static KdiEntryBuilder builder(){
        return new KdiEntryBuilder();
    }
    public static class KdiEntryBuilder{
        String username;
        String password;
        String DpsPath;
        String SignaturePath;
        String isSignature;
        String SecretId;
        String SecretKey;
        String Signature;

        public KdiEntryBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public KdiEntryBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public KdiEntryBuilder setDpsPath(String dpsPath) {
            DpsPath = dpsPath;
            return this;
        }

        public KdiEntryBuilder setSignaturePath(String signaturePath) {
            SignaturePath = signaturePath;
            return this;
        }

        public KdiEntryBuilder setIsSignature(String isSignature) {
            this.isSignature = isSignature;
            return this;
        }

        public KdiEntryBuilder setSecretId(String secretId) {
            SecretId = secretId;
            return this;
        }

        public KdiEntryBuilder setSecretKey(String secretKey) {
            SecretKey = secretKey;
            return this;
        }

        public KdiEntryBuilder setSignature(String signature) {
            Signature = signature;
            return this;
        }
        public KdiEntry build(){
            return new KdiEntry(username,password,DpsPath,SignaturePath,isSignature,SecretId,SecretKey,Signature);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDpsPath() {
        return DpsPath;
    }

    public String getSignaturePath() {
        return SignaturePath;
    }

    public String getIsSignature() {
        return isSignature;
    }

    public String getSecretId() {
        return SecretId;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public String getSignature() {
        return Signature;
    }
}
