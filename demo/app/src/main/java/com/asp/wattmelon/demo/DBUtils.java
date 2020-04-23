package com.asp.wattmelon.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wattmelon on 2020/1/25.
 */

public class DBUtils {

    private static String driver = "com.mysql.jdbc.Driver";//MySQL驱动
    private static String url = "jdbc:mysql://10.0.2.2:3306/demo";//MYSQL数据库连接Url,在虚拟机中，10.0.2.2指的就是电脑的Ip 地址
    private static String user = "local";
    private static String password = "123456";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    //用户登录，保存用户信息
    public static Map<String, String> login(User user) {
        HashMap<String, String> map = new HashMap<>();
        Connection conn = getConnection();
        try {
            Statement st = conn.createStatement();
            String sql = "select * from users where uid ='" + user.getUserid()
                    + "' and password ='" + user.getPassword() + "'";
            ResultSet res = st.executeQuery(sql);
            if ( res == null ) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                res.next();
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnName(i);
                    map.put(field, res.getString(field));
                }
                closeAll(res,st,conn);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //关闭连接
    public static void closeAll(ResultSet rs, Statement st, Connection con) {
        try {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
             if ( con != null ) {
                 con.close();
             }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //判断该产品列表中是否有该产品
    public static boolean isExist(ArrayList<Product> products, int pid) {//pid为数据库查询结果 products为已存在列表
        for (Product product : products)
            if ( product.getPid() == pid )
                return true;
        return false;
    }

}


