package com.hn;

import com.hn.listener.CatEventHandler;
import com.hn.Entry.BaiDuEntry;
import com.hn.common.ShareData;

import com.hn.utils.file.FileUtil;
import com.hn.utils.http.CatApiUtil;
import com.hn.utils.http.HttpUtil;
import com.hn.utils.timing.TimingUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.BotConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public final class Cat extends JavaPlugin {
    public static final Cat INSTANCE = new Cat();
    private Cat() {
        super(new JvmPluginDescriptionBuilder(
                "com.hn.cat", "1.0.0")
                .name("cat")
                .author("Summer")
                .build());
    }
    @Override
    public void onEnable() {
        //初始化插件
        CatInit();
//        Bot bot = BotFactory.INSTANCE.newBot(2680785606L, BotAuthorization.byQRCode(), configuration -> {
//            configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
//        });
//        bot.login();
        //创建监听器
        GlobalEventChannel.INSTANCE.registerListenerHost(new CatEventHandler());
    }

    @SuppressWarnings("unchecked")
    public void CatInit(){
        try {
            //配置文件
            File MapFile = resolveConfigFile("marry.properties");
            if (!MapFile.exists()) {
                MapFile.createNewFile();
            }
            //初始化全局配置类
            ShareData instance = ShareData.getInstance();

            //创建定时器
            TimingUtil timingUtil = new TimingUtil();
            //设置一个每天凌晨0点执行的定时任务
            timingUtil.startTimedTasks(0, 0, 0, MapFile);
            //设置一个每五分钟调用的定时器
            timingUtil.startFiveMinutesTasks(MapFile);
            Yaml yaml = new Yaml();
            //群老婆列表
            LinkedHashMap<Long, Long> map = FileUtil.readMap(MapFile);
            //设置群老婆Map;
            if (map != null && !map.isEmpty()) {
                instance.setMarryMap(map);
            }
            //获取配置文件

            File ConfigFile = resolveConfigFile("miaomiao.yml");
            if (!ConfigFile.exists()) {
                ConfigFile.createNewFile();
            }
            //设置配置文件
            instance.setResolveConfigFile(ConfigFile);
            //获取文件读取流
            FileInputStream fileInputStream = new FileInputStream(ConfigFile);
            //获取yml配置
            Map<String, Object> data = yaml.load(fileInputStream);
            //获取所有的群
            List<Integer> groups = (List<Integer>) data.get("权限群列表");
            instance.setGroups(groups);
            //获取查询服务器列表
            Map<String, String> addressesMap = (HashMap<String, String>) data.get("服务器列表");
            instance.setAddresses(addressesMap);
            //获取配置
            Map<String, String> CatApiMap = (Map<String, String>) data.get("代理域名");
            instance.setCatApiUtil(new CatApiUtil(CatApiMap.get("url")));
            //获取需要检测发言的群号
            List<Integer> prohibitGroup = (List<Integer>) data.get("检测群列表");
            instance.setProhibitGroup(prohibitGroup);
            //获取插件管理人员
            List<Integer> Manager = (List<Integer>) data.get("插件管理人员");
            instance.setManager(Manager);
            //获取百度图片检测配置
            Map<String, Object> BaiDuMap = (Map<String, Object>) data.get("百度配置");
            BaiDuEntry baiDuEntry = BaiDuEntry.builder()
                    .setApiKey(BaiDuMap.get("ApiKey").toString())
                    .setSecretKey(BaiDuMap.get("SecretKey").toString())
                    .setAccessToken(HttpUtil.getAccessToken(BaiDuMap.get("ApiKey").toString(), BaiDuMap.get("SecretKey").toString()))
                    .build();
            instance.setBaiDuEntry(baiDuEntry);
            //开启内容检测
            instance.setOpenProhibit(true);
            System.out.println("初始化成功...");
        } catch (Exception e) {
            System.out.println("配置初始化失败,请检查配置参数!!!");
        }
    }
}