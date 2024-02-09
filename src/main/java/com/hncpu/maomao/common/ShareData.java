package com.hncpu.maomao.common;

import com.hncpu.maomao.Entry.BaiDuEntry;
import com.hncpu.maomao.utils.http.CatApiUtil;

import java.io.File;
import java.util.*;

public class ShareData {
    // 工具类实例对象
    private static volatile ShareData instance;
    // 老婆列表
    private LinkedHashMap<Long, Long> MarryMap;
    //订婚列表
    private List<Long> DingHunList;
    //离婚列表
    private List<Long> LiHunList;
    //小三列表
    private List<Long> XiaoSanList;
    //服务器列表
    private Map<String,String> Addresses;
    //代理请求类
    private CatApiUtil catApiUtil;
    //获取有权限的群
    private List<Integer> Groups;
    //需要检测的群
    private List<Integer> ProhibitGroup;
    //百度配置
    private BaiDuEntry baiDuEntry;
    //配置文件
    private File ResolveConfigFile;
    //插件管理人员
    private List<Integer> Manager;
    public static ShareData getInstance(){
        if (instance == null){
            synchronized (ShareData.class){
                if (instance == null){
                    instance = new ShareData();
                    //初始化群老婆列表
                    instance.setMarryMap(new LinkedHashMap<>());
                    // 初始化订婚
                    instance.setDingHunList(new ArrayList<>());
                    // 初始化小三
                    instance.setXiaoSanList(new ArrayList<>());
                    // 初始化离婚
                    instance.setLiHunList(new ArrayList<>());
                }
            }
        }
        return instance;
    }

    public LinkedHashMap<Long, Long> getMarryMap() {
        return MarryMap;
    }

    public void setMarryMap(LinkedHashMap<Long, Long> marryMap) {
        MarryMap = marryMap;
    }

    public List<Long> getDingHunList() {
        return DingHunList;
    }

    public void setDingHunList(List<Long> dingHunList) {
        DingHunList = dingHunList;
    }

    public List<Long> getLiHunList() {
        return LiHunList;
    }

    public void setLiHunList(List<Long> liHunList) {
        LiHunList = liHunList;
    }

    public List<Long> getXiaoSanList() {
        return XiaoSanList;
    }

    public void setXiaoSanList(List<Long> xiaoSanList) {
        XiaoSanList = xiaoSanList;
    }

    public Map<String, String> getAddresses() {
        return Addresses;
    }

    public void setAddresses(Map<String, String> addresses) {
        Addresses = addresses;
    }

    public CatApiUtil getCatApiUtil() {
        return catApiUtil;
    }

    public void setCatApiUtil(CatApiUtil catApiUtil) {
        this.catApiUtil = catApiUtil;
    }

    public List<Integer> getGroups() {
        return Groups;
    }

    public void setGroups(List<Integer> groups) {
        Groups = groups;
    }

    public List<Integer> getProhibitGroup() {
        return ProhibitGroup;
    }

    public void setProhibitGroup(List<Integer> prohibitGroup) {
        ProhibitGroup = prohibitGroup;
    }

    public BaiDuEntry getBaiDuEntry() {
        return baiDuEntry;
    }
    public void setBaiDuEntry(BaiDuEntry baiDuEntry) {
        this.baiDuEntry = baiDuEntry;
    }

    public File getResolveConfigFile() {
        return ResolveConfigFile;
    }

    public void setResolveConfigFile(File resolveConfigFile) {
        ResolveConfigFile = resolveConfigFile;
    }

    public static void setInstance(ShareData instance) {
        ShareData.instance = instance;
    }

    public List<Integer> getManager() {
        return Manager;
    }

    public void setManager(List<Integer> manager) {
        Manager = manager;
    }
}

