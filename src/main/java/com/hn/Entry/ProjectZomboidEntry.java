package com.hn.Entry;

public class ProjectZomboidEntry {
    //服务器地址
    private final String addr;
    //服务器名称
    private final String name;
    //游戏ID
    private final Integer appid;
    //游戏版本
    private final String version;
    //游戏项目名称
    private final String product;
    //在线玩家
    private final Integer players;
    //最大玩家数
    private final Integer max_players;

    private ProjectZomboidEntry(String addr, String name, Integer appid, String version, String product, Integer players, Integer max_players) {
        this.addr = addr;
        this.name = name;
        this.appid = appid;
        this.version = version;
        this.product = product;
        this.players = players;
        this.max_players = max_players;
    }
    public static ProjectZomboidBuilder builder(){
        return new ProjectZomboidBuilder();
    }
    public static class ProjectZomboidBuilder{
        //服务器地址
        String addr;
        //服务器名称
        String name;
        //游戏ID
        Integer appid;
        //游戏版本
        String version;
        //游戏项目名称
        String product;
        //在线玩家
        Integer players;
        //最大玩家数
        Integer max_players;

        public ProjectZomboidBuilder setAddr(String addr) {
            this.addr = addr;
            return this;
        }

        public ProjectZomboidBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProjectZomboidBuilder setAppid(Integer appid) {
            this.appid = appid;
            return this;
        }

        public ProjectZomboidBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

        public ProjectZomboidBuilder setProduct(String product) {
            this.product = product;
            return this;
        }

        public ProjectZomboidBuilder setPlayers(Integer players) {
            this.players = players;
            return this;
        }

        public ProjectZomboidBuilder setMax_players(Integer max_players) {
            this.max_players = max_players;
            return this;
        }
        public ProjectZomboidEntry build(){
            return new ProjectZomboidEntry(addr,name,appid,version,product,players,max_players);
        }
    }

    public String getAddr() {
        return addr;
    }

    public String getName() {
        return name;
    }

    public Integer getAppid() {
        return appid;
    }

    public String getVersion() {
        return version;
    }

    public String getProduct() {
        return product;
    }

    public Integer getPlayers() {
        return players;
    }

    public Integer getMax_players() {
        return max_players;
    }
}
