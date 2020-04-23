package com.asp.wattmelon.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDao {
    public ArrayList<User> getAllUsers(){
        ArrayList<User> result = null;
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            String sql = "SELECT * FROM users;";

            con= DBUtils.getConnection();
            st=con.createStatement();
            result = new ArrayList<User>();
            rs = st.executeQuery(sql);

            while(rs.next()){
                User user = new User();
                user.setPassword(rs.getString("password"));
                user.setUserid(rs.getInt("uid"));
                result.add(user);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return result;

    }
}
