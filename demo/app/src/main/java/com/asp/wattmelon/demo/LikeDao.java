package com.asp.wattmelon.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class LikeDao {
    public ArrayList<Like> findLikesByUser(int uid){
        ArrayList<Like> result = null;
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            String sql="SELECT * FROM likes where uid = "+uid+";";

            con= DBUtils.getConnection();
            st=con.createStatement();
            result = new ArrayList<Like>();
            rs = st.executeQuery(sql);

            while(rs.next()){
                Like like = new Like();
                like.setLid(rs.getInt("lid"));
                like.setPid(rs.getInt("pid"));
                like.setUid(rs.getInt("uid"));

                result.add(like);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return result;
    }

    public static void addLike(int uid, int pid){
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            con= DBUtils.getConnection();
            st=con.createStatement();
            String sql="INSERT INTO `likes` (`uid`, `pid`) VALUES ('"+uid+"', '"+pid+"');";
            st.execute(sql);
            String sql1="update products set likeCnt=likeCnt+1 where pid="+pid+";";
            st.execute(sql1);
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
    }

    public static void removeLike(int uid, int pid){
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;

        try {
            con= DBUtils.getConnection();
            st=con.createStatement();
            String sql="DELETE FROM `likes` WHERE uid="+uid+" and pid="+pid+";";
            st.execute(sql);
            String sql1="update products set likeCnt=likeCnt-1 where pid="+pid+";";
            st.execute(sql1);
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
    }
    public static boolean isLiked(int uid, int pid){
        Connection con=null;
        ResultSet rs=null;
        Statement st=null;
        boolean liked=false;
        try{
            String sql = "select lid from likes where uid="+uid+" and pid="+pid+";";

            con= DBUtils.getConnection();
            st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(sql);
            rs.last();
            if ( rs.getRow()!=0 )
                liked=true;
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.closeAll(rs, st, con);
        }
        return liked;
    }
}
