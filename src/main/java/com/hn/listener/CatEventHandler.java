package com.hn.listener;

import com.hn.Entry.BaiDuEntry;
import com.hn.common.ShareData;
import com.hn.message.CatSendMessage;
import com.hn.utils.file.FileUtil;
import com.hn.utils.http.CatApiUtil;
import com.hn.utils.http.HttpUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatEventHandler extends SimpleListenerHost {
    public CatEventHandler() {

    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) {
        //获取群消息
        MessageChain message = event.getMessage();
        //获取有权限的群列表
        List<Integer> groups = ShareData.getInstance().getGroups();
        //获取当前群号
        Integer group = Math.toIntExact(event.getGroup().getId());
        //查看是否是视频等不能发送的东西
        if (prohibit(event, group)) return;
        //判断群是否有权限
        if (groups.contains(group)) {
            if (message.get(1) instanceof PlainText) {
                String Type = ((PlainText) message.get(1)).getContent();
                send(event, Type);
            }
        }
    }
    @EventHandler
    @SuppressWarnings("unchecked")
    public void onFriendMessage(@NotNull FriendMessageEvent event){
        //获取群消息
        MessageChain message = event.getMessage();
        if (message.get(1) instanceof PlainText) {
            String Type = ((PlainText) message.get(1)).getContent();
            if (Type.equals("配置信息")){
                //检测是否是拥有权限的人操作
                ShareData instance = ShareData.getInstance();
                //发起者QQ号
                Integer fromQQ= (int) event.getSender().getId();
                if(!instance.getManager().contains(fromQQ)){
                    return;
                }
                //拥有权限的群
                List<Integer> groups = instance.getGroups();
                //服务器列表
                Map<String, String> addresses = instance.getAddresses();
                //需要检测发言的群
                List<Integer> prohibitGroup = instance.getProhibitGroup();
                //插件管理人员
                List<Integer> manager = instance.getManager();
                //构建消息链
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                messageChainBuilder.append("======权限群列表======\n");
                groups.forEach(item->{
                    messageChainBuilder.append(item.toString()).append("\n");
                });
                messageChainBuilder.append("======服务器列表======\n");
                addresses.forEach((key,value)->{
                    messageChainBuilder.append(key).append(" : ").append(value).append("\n");
                });
                messageChainBuilder.append("======检测群列表======\n");
                prohibitGroup.forEach(item->{
                    messageChainBuilder.append(item.toString()).append("\n");
                });
                messageChainBuilder.append("======插件管理人员======\n");
                manager.forEach(item->{
                    messageChainBuilder.append(item.toString()).append("\n");
                });
                messageChainBuilder.append("=======检测开关=======\n");
                messageChainBuilder.append(instance.getOpenProhibit() ? "开启" : "关闭").append("\n");
                MessageChain messages = messageChainBuilder.build();
                //发送配置文件信息
                event.getSender().sendMessage(messages);
            }
            if (Type.contains("修改配置")){
                //处理参数
                String[] commands = Type.split(" ");
                //检测是否是拥有权限的人操作
                ShareData instance = ShareData.getInstance();
                //发起者QQ号
                Integer fromQQ= (int) event.getSender().getId();
                if(!instance.getManager().contains(fromQQ)){
                    return;
                }
                File resolveConfigFile = instance.getResolveConfigFile();
                try {
                    Reader reader= new InputStreamReader(Files.newInputStream(resolveConfigFile.toPath()), StandardCharsets.UTF_8);
                    Yaml yaml1=new Yaml();
                    Map<String,Object> data = yaml1.load(reader);
                    String command = commands[2];
                    switch (commands[1]){
                        case "权限群列表":
                            List<Integer> groups= (List<Integer>)data.get("权限群列表");
                            if (command.equals("添加")){
                                Integer group = Integer.valueOf(commands[3]);
                                groups.add(group);
                            }else if(command.equals("删除")){
                                Integer group = Integer.valueOf(commands[3]);
                                groups.remove(group);
                            }
                            break;
                        case "服务器列表":
                            Map<String,String> addressesMap= (HashMap<String, String>)data.get("服务器列表");
                            switch (command) {
                                case "添加":
                                case "修改":
                                    addressesMap.put(commands[3], commands[4]);
                                    break;
                                case "删除":
                                    addressesMap.remove(commands[3]);
                                    break;
                            }
                            break;
                        case "检测群列表":
                            List<Integer> prohibitGroup= (List<Integer>)data.get("检测群列表");
                            if (command.equals("添加")){
                                Integer group = Integer.valueOf(commands[3]);
                                prohibitGroup.add(group);
                            }else if(command.equals("删除")){
                                Integer group = Integer.valueOf(commands[3]);
                                prohibitGroup.remove(group);
                            }
                            break;
                        case "插件管理人员":
                            List<Integer> Manager= (List<Integer>)data.get("插件管理人员");
                            if (command.equals("添加")){
                                Integer qq = Integer.valueOf(commands[3]);
                                Manager.add(qq);
                            }else if(command.equals("删除")){
                                Integer qq = Integer.valueOf(commands[3]);
                                Manager.remove(qq);
                            }
                            break;
                        case "检测开关":
                            if (command.equals("修改")){
                                instance.setOpenProhibit(Boolean.parseBoolean(commands[3]));
                            }
                            break;
                        default:
                            event.getSender().sendMessage("指令错误...");
                            return;
                    }
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(resolveConfigFile.toPath()), StandardCharsets.UTF_8);
                    yaml1.dump(data,outputStreamWriter);
                    outputStreamWriter.close();
                    loadShareData();
                    event.getSender().sendMessage("修改成功...");
                }catch (Exception e){
                    event.getSender().sendMessage("修改失败:"+e.getMessage());
                }
            }
        }
    }
    //重新加载配置类

    @SuppressWarnings("unchecked")
    public void loadShareData() throws IOException {
        ShareData instance = ShareData.getInstance();
        File ConfigFile = instance.getResolveConfigFile();
        Reader reader= new InputStreamReader(Files.newInputStream(ConfigFile.toPath()), StandardCharsets.UTF_8);
        Map<String,Object> data = new Yaml().load(reader);
        //获取所有的群
        List<Integer> groups= (List<Integer>) data.get("权限群列表");
        instance.setGroups(groups);
        //获取查询服务器列表
        Map<String,String> addressesMap= (HashMap<String, String>) data.get("服务器列表");
        instance.setAddresses(addressesMap);
        //获取配置
        Map<String,String> CatApiMap= (Map<String, String>) data.get("代理域名");
        instance.setCatApiUtil(new CatApiUtil(CatApiMap.get("url")));
        //获取需要检测发言的群号
        List<Integer> prohibitGroup = (List<Integer>) data.get("检测群列表");
        instance.setProhibitGroup(prohibitGroup);
        //获取插件管理人员
        List<Integer> Manager= (List<Integer>) data.get("插件管理人员");
        instance.setManager(Manager);
        //获取百度图片检测配置
        Map<String,Object> BaiDuMap= (Map<String, Object>) data.get("百度配置");
        BaiDuEntry baiDuEntry = BaiDuEntry.builder()
                .setApiKey(BaiDuMap.get("ApiKey").toString())
                .setSecretKey(BaiDuMap.get("SecretKey").toString())
                .setAccessToken(HttpUtil.getAccessToken(BaiDuMap.get("ApiKey").toString(),BaiDuMap.get("SecretKey").toString()))
                .build();
        instance.setBaiDuEntry(baiDuEntry);
    }
    //匹配发送信息
    public void send(GroupMessageEvent event, String Type) {
        switch (Type) {
            case "娶群友":
                CatSendMessage.Marry(event);
                break;
            case "订婚":
                CatSendMessage.Engage(event);
                break;
            case "离婚":
                CatSendMessage.Divorce(event);
                break;
            case "当小三":
                CatSendMessage.Mistress(event);
                break;
            case "群老婆列表":
                CatSendMessage.getWifeList(event);
                break;
            case "喵服":
                CatSendMessage.getMiaoFuList(event);
                break;
            case "奶服":
                CatSendMessage.getNaiFuList(event);
                break;
            default:
                //未知信息
                break;
        }
    }

    //处理非法信息
    public boolean prohibit(GroupMessageEvent event, Integer group) {
        ShareData instance = ShareData.getInstance();
        if (!instance.getOpenProhibit()){
            return false;
        }
        MessageChain message = event.getMessage();
        //需要检测的群
        List<Integer> prohibitGroup = instance.getProhibitGroup();
        if (!prohibitGroup.contains(group)) {
            return false;
        }
        //检测群友是不是管理员或群主 是则不检测
        int level = event.getSender().getPermission().getLevel();
        if (level != 0) {
            return false;
        }
        //处理非法信息
        for (SingleMessage singleMessage : message) {
            String input = singleMessage.contentToString();
            switch (input) {
                case "[视频]":
                    sendProhibit(message, event, "禁止视频(防炸群)");
                    return true;
                case "[转发消息]":
                    sendProhibit(message, event, "禁止转发消息(防炸群)");
                    return true;
                case "[图片]":
                case "[动画表情]":
                    //检测消息里的每一张图片是否合格 不合格返回false
                    prohibitImage(event, message, instance, (Image) singleMessage);
                    break;
                default:
                    //其他消息则通过
                    break;
            }
        }
        return false;
    }

    //发送禁止消息
    public void sendProhibit(MessageChain message, GroupMessageEvent event, String msg) {
        try {
            MessageSource.recall(message);
            event.getGroup().sendMessage(msg);
        } catch (Exception e) {
            System.out.println("无权限撤回");
        }
    }

    //检测图片是否合法
    public void prohibitImage(GroupMessageEvent event, MessageChain message, ShareData instance, Image Img) {
        System.out.println("检测中...");
        BaiDuEntry baiDuEntry = instance.getBaiDuEntry();
        String imageUrl = Image.queryUrl(Img);
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String fileContentAsBase64 = FileUtil.getFileContentAsBase64(imageUrl, true);
            RequestBody body = RequestBody.create(mediaType, "image=" + fileContentAsBase64);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined?access_token=" + baiDuEntry.getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = new OkHttpClient().newBuilder().build().newCall(request).execute();
            JSONObject rootNode = new JSONObject(response.body().string());
            if (!rootNode.get("conclusion").equals("合规")) {
                try {
                    JSONObject dataNode = (JSONObject) rootNode.getJSONArray("data").get(0);
                    Member sender = event.getSource().getSender();
                    //获取管理员对象
                    Group group = event.getBot().getGroup(305676975L);
                    if (group != null) {
                        //向管理员发送违规图片
                        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                        messageChainBuilder.append("违规人员QQ号 : ").append(String.valueOf(sender.getId()));
                        messageChainBuilder.append(Img);
                        group.sendMessage(messageChainBuilder.build());
                    }
                    //发送违规信息
                    sendProhibit(message, event, "禁止发违规图片(原因:" + dataNode.get("msg") + ",发起者:" + sender.getId() + ")");
                } catch (Exception e) {
                    System.out.println("无权限禁言|撤回时发生异常");
                }
            }
        } catch (IOException e) {
            //图片检测失败 查询获取AccessToken
            System.out.println("图片检测失败" + e.getMessage());
            String accessToken = HttpUtil.getAccessToken(baiDuEntry.getApiKey(), baiDuEntry.getSecretKey());
            if (accessToken != null) {
                instance.getBaiDuEntry().setAccessToken(accessToken);
            }
        }
    }
}
