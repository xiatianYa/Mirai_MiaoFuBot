package com.hncpu.maomao.utils.http;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static InputStream input;
    private static ExternalResource externalResource;
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    //获取用户QQ头像
    public static String getQQImage(Long qq, MessageEvent event) {
        try {
            URL url = new URL("http://q.qlogo.cn/g?b=qq&nk="+qq+"&s=640");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            input = con.getInputStream();
            externalResource = ExternalResource.create(input);
            Contact contact = event.getSubject();
            Image image = contact.uploadImage(externalResource);
            return image.getImageId();
        }catch(Exception e){
            String message = e.getMessage();
            return "图片获取失败"+message;
        }finally {
            try {
                input.close();
                externalResource.close();
            }catch (Exception e){
                System.out.println("资源关闭失败");
            }
        }
    }
    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     */
    public static String getAccessToken(String API_KEY,String SECRET_KEY){
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            return new JSONObject(response.body().string()).getString("access_token");
        }catch (IOException e){
            System.out.println("签名获取失败:"+e.getMessage());
            return null;
        }
    }
}
