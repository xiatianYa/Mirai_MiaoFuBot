package com.hn.utils.http;

import com.hn.Entry.ProjectZomboidEntry;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CatApiUtil {
    //查询的基本地址
    private final String path;

    public CatApiUtil(String path) {
        this.path = path;
    }

    //获取服务器信息
    public List<ProjectZomboidEntry> getMessage(Map<String, String> paths) {
        // 创建一个固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(paths.size());
        // 保存结果的List
        List<ProjectZomboidEntry> resultList = new ArrayList<>();
        // 获取当前时间（开始时间）
        long startTime = System.currentTimeMillis();
        // 创建并启动线程来调用方法
        paths.forEach((key, value) -> {
            executorService.submit(() -> {
                resultList.add(handleParam(key, value, sendHttpRequest(path + value))); // 将返回值添加到List集合中
            });
        });
        // 关闭线程池
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("请求超时");
        }
        // 获取当前时间（结束时间）
        long endTime = System.currentTimeMillis();
        // 计算方法执行时间（毫秒）
        long elapsedTime = endTime - startTime;
        // 输出方法执行时间
        System.out.println("获取服务器信息耗时 : " + elapsedTime + "秒");
        return resultList;
    }

    //发送请求的接口
    private static Response sendHttpRequest(String url) {
        //发送网络请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            return response;
        } catch (Exception e) {
            System.out.println("请求发送失败:" + e.getMessage());
            return null;
        }
    }

    //处理返回参数
    public ProjectZomboidEntry handleParam(String key, String value, Response response) {
        JSONObject rootNode = null;
        try {
            if (response.body() != null) {
                rootNode = new JSONObject(response.body().string());
            }
            JSONObject responseNode = null;
            if (rootNode != null) {
                responseNode = rootNode.getJSONObject("response");
            }
            JSONArray serversNode = null;
            if (responseNode != null) {
                serversNode = responseNode.getJSONArray("servers");
            }
            JSONObject serverNode = null;
            // 取第一个服务器，可以根据需要调整
            if (serversNode != null) {
                serverNode = serversNode.getJSONObject(0);
            }
            if (serverNode != null) {
                return ProjectZomboidEntry.builder()
                        .setAddr(serverNode.getString("addr"))
                        .setName(serverNode.getString("name"))
                        .setAppid(serverNode.getInt("appid"))
                        .setVersion(serverNode.getString("version"))
                        .setProduct(serverNode.getString("product"))
                        .setPlayers(serverNode.getInt("players"))
                        .setMax_players(serverNode.getInt("max_players"))
                        .build();
            } else {
                System.out.println("服务器信息获取失败");
                return ProjectZomboidEntry.builder()
                        .setName(key)
                        .setAddr(value)
                        .setVersion("-1")
                        .build();
            }
        } catch (Exception e) {
            System.out.println("服务器信息获取失败");
            return ProjectZomboidEntry.builder()
                    .setName(key)
                    .setAddr(value)
                    .setVersion("-1")
                    .build();
        }
    }
}
