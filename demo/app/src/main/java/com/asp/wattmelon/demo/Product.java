package com.asp.wattmelon.demo;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    int pid;        //id
    String title;   //标题
    int cnt;        //点赞数
    double w;       //相似度(只在推荐时调用)

    public Product() {}

    public Product(int pid, String title, int cnt) {
        this.pid = pid;
        this.title = title;
        this.cnt = cnt;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }


    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", title='" + title + '\'' +
                ", cnt=" + cnt +
                ", w=" + w +
                '}';
    }
}

