package com.asp.wattmelon.demo;


import java.util.*;

public class ItemSimilarity {

    //通过计算余弦相似度并取TopN, 返回为uid的用户生成的5个推荐文章
    public static ArrayList<Product> recommend(int uid){
        UserDao userdao = new UserDao();
        ProductDao productdao = new ProductDao();
        LikeDao likedao = new LikeDao();

        ArrayList<Like> likeLists;                                       //其他用户点赞产品列表

        ArrayList<User> users = userdao.getAllUsers();                   //所有用户列表
        ArrayList<Product> products = productdao.getAllProducts();               //所有产品列表

        int[][] curMatrix = new int[products.size()+5][products.size()+5];   //当前矩阵
        int[][] comMatrix = new int[products.size()+5][products.size()+5];   //共现矩阵
        int[] N = new int[products.size()+5];                              //喜欢每个物品的人数

        for(User user: users) {
            if ( user.getUserid() == uid ) continue;                    //当前登录用户跳过
            likeLists = likedao.findLikesByUser(user.getUserid());      //当前用户的点赞列表

            //矩阵初始化
            for (int i = 0; i < products.size(); i++)
                for (int j = 0; j < products.size(); j++)
                    curMatrix[i][j] = 0;

            //初始矩阵
            for (int i = 0; i < likeLists.size(); i++) {
                int pid1 = likeLists.get(i).getPid();
                ++N[pid1];                                          //喜欢该id物品的人数加一
                for (int j = i + 1; j < likeLists.size(); j++) {
                    int pid2 = likeLists.get(j).getPid();
                    ++curMatrix[pid1][pid2];
                    ++curMatrix[pid2][pid1];                        //对称加一
                }
            }
            //累加所有矩阵, 得到共现矩阵
            for (int i = 0; i < products.size(); i++) {
                for (int j = 0; j < products.size(); j++) {
                    int pid1 = products.get(i).getPid(), pid2 = products.get(j).getPid();
                    comMatrix[pid1][pid2] += curMatrix[pid1][pid2];
                    comMatrix[pid1][pid2] += curMatrix[pid1][pid2];
                }
            }
        }

        TreeSet<Product> preList = new TreeSet<Product>(new Comparator<Product>() {
            //重写compare方法  按相似度Wij排序；当相似度Wij相同时，按点赞数排序
            @Override
            public int compare(Product o1, Product o2) {
                if(o1.getW()!=o2.getW()){
                    return (int) ((o1.getW()-o2.getW())*100); //返回值为0，表示同一元素
                }
                else{
                    //当相似度相同时，比较点赞数
                    return o1.getCnt()-o2.getCnt();
                }
            }
        }); //预处理的列表

        likeLists = likedao.findLikesByUser(uid);       //当前登录用户点赞产品列表

        boolean[] used = new boolean[products.size()+5];  //判断是否已加入推荐列表

        for(Like like: likeLists){
            int Nij = 0;                         //既喜欢i又喜欢j的人数
            double Wij;                          //相似度
            Product tmp;                           //当前的产品

            int i = like.getPid();

            for(Product product: products){
                if(like.getPid() == product.getPid()) continue;
                int j = product.getPid();
                Nij = comMatrix[i][j];
                Wij = (double)Nij/Math.sqrt(N[i]*N[j]);         //计算余弦相似度

                tmp = productdao.findProductById(product.getPid());

                if(Double.isNaN(Wij)) tmp.setW(0);
                else tmp.setW(Wij);

                if(used[tmp.getPid()]) continue;               //已加入推荐列表

//                System.out.println("i__"+i+"  j__"+j+"   Nij__"+Nij+"   Wij__"+Wij+"    条件判断"+!Double.isNaN(Wij)+"   Wij数值"+Wij);

                if(!Double.isNaN(Wij) && Wij!=0){
                    preList.add(tmp);
                    used[tmp.getPid()] = true;
                }
            }
        }

        ArrayList<Product> recomLists = new ArrayList<>();      //生成的推荐结果

        for(int i = 0; preList.size()>0 && i<5; i++){
            recomLists.add(preList.pollLast());
        }

        //推荐数量不满5个, 补足喜欢数最高的文章，考虑重复
        if(recomLists.size()<5){
            recomLists = productdao.findTopNProducts(recomLists);
        }

        return recomLists;
    }
}