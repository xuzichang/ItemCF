package com.asp.wattmelon.demo;

public class Like {
    int uid; //点赞用户id
    int pid; //点赞产品id
    int lid; //记录点赞行为id

    public Like(){}

    public Like(int uid, int pid, int lid) {
        this.uid = uid;
        this.pid = pid;
        this.lid = lid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    @Override
    public String toString() {
        return "Like{" +
                "uid=" + uid +
                ", pid=" + pid +
                ", lid=" + lid +
                '}';
    }
}
