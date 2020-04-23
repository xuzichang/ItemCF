package com.asp.wattmelon.demo;

import java.io.Serializable;

/**
 * Created by wattmelon on 2020/1/25.
 */

public class User  implements Serializable {
    int userid;            //用户id
    private String password;

    public User() {}

    public User(int userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPassword() { return password;}

    public void setPassword(String password) {
        this.password = password;
    }

}