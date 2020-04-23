package com.asp.wattmelon.demo;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDao {
    public Product findProductById(int pid){
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        Product product = new Product();
        try {
            String sql="SELECT * FROM products where pid = "+pid+";";

            con= DBUtils.getConnection();
            st=con.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()){
                product.setPid(rs.getInt("pid"));
                product.setCnt(rs.getInt("likeCnt"));
                product.setTitle(rs.getString("title"));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return product;
    }

    public ArrayList<Product> findTopNProducts(ArrayList<Product> products){
        //将点赞最多的产品补入推荐列表, 直至满5个(考虑重复)
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            String sql = "SELECT * FROM products order by likeCnt desc;";

            con= DBUtils.getConnection();
            st=con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next() && products.size() < 5){
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setCnt(rs.getInt("likeCnt"));
                product.setTitle(rs.getString("title"));

                if(!DBUtils.isExist(products, product.getPid()))
                    products.add(product);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return products;
    }

    public static ArrayList<Product> findRemainProducts(ArrayList<Product> products){
        //将剩余的产品补入剩余列表(考虑重复)
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            String sql = "SELECT * FROM products";

            con= DBUtils.getConnection();
            st=con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setCnt(rs.getInt("likeCnt"));
                product.setTitle(rs.getString("title"));

                if(!DBUtils.isExist(products, product.getPid())) {//如果不存在，添加
                    products.add(product);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return products;
    }


    public static  ArrayList<Product> getAllProducts(){
        ArrayList<Product> result = null;
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            String sql = "SELECT * FROM products;";
            con= DBUtils.getConnection();
            st=con.createStatement();
            result = new ArrayList<>();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setCnt(rs.getInt("likeCnt"));
                product.setTitle(rs.getString("title"));
                result.add(product);

            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return result;
    }

    public void addLikeCnt(int pid){
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            String sql = "UPDATE `products` SET `likeCnt` = likeCnt+'1' WHERE (`pid` = '"+pid+"');";

            con= DBUtils.getConnection();
            st=con.createStatement();
            st.execute(sql);
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
    }
}
