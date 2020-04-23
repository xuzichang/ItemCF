package com.asp.wattmelon.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import static com.asp.wattmelon.demo.LikeDao.addLike;
import static com.asp.wattmelon.demo.LikeDao.isLiked;
import static com.asp.wattmelon.demo.LikeDao.removeLike;
import static com.asp.wattmelon.demo.ProductDao.findRemainProducts;


/**
 * Created by wattmelon on 2020/1/25.
 */

public class infoActivity extends AppCompatActivity {
    TextView password, recommandid;
    User user;
    List<Product> infoList;

    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Handler handler;
    ArrayList<Product> productList = new ArrayList<>();//存放推荐产品

    private OnLoadMoreListener mOnLoadMoreListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_info);

            password =(TextView)findViewById(R.id.info_password);
            recommandid = (TextView)findViewById(R.id.info_recomid);

            Intent getData=getIntent();
            user = (User)getData.getSerializableExtra("user");//获取传递的参数user
            infoList = (ArrayList) getIntent().getSerializableExtra("product");//获取推荐的结果列表

            recommandid.setText("Your userid is "+user.getUserid()+"，以下是该id生成的推荐");
            password.setText("Your password is "+user.getPassword());//验证登录
            try {
                init();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    //判断是否点赞该产品
    class DBThread extends Thread{
        int i;
        boolean result=false;
        public void setI(int i) {
            this.i = i;
        }
        public boolean getResult(){return result;}
        public void run() {
            result=isLiked(user.getUserid(),i);
        }
    }
    private boolean checkLike(int i) throws InterruptedException {
        DBThread thread = new DBThread();
        thread.setI(i);
        thread.start();
        thread.join();//等线程结束后再获取值，很卡
        return thread.getResult();
    }

    //添加点赞
    private void addLikes(int i){
        new Thread() {
            public void run() {
                try {
                    addLike(user.getUserid(),i);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    //取消点赞
    private void removeLikes(int i){
        new Thread() {
            public void run() {
                try {
                    removeLike(user.getUserid(),i);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //推荐产品
    class DBThread4 extends Thread{
        ArrayList<Product> result;
        public ArrayList<Product> getResult(){return result;}
        public void run(){
            result= ItemSimilarity.recommend(user.getUserid());
        }
    }
    private ArrayList<Product> recommandProducts() throws InterruptedException {
        DBThread4 thread = new DBThread4();
        thread.start();
        thread.join();
        return thread.getResult();
    }

    //获取剩余产品用于加载更多
    class DBThread5 extends Thread{
        ArrayList<Product> result;
        public ArrayList<Product> getResult(){return result;}
        public void run(){
            result= findRemainProducts(productList);

        }
    }
    private ArrayList<Product> loadMoreProducts() throws InterruptedException {
        DBThread5 thread = new DBThread5();
        thread.start();
        thread.join();
        return thread.getResult();
    }

    //初始化
    private void init() throws InterruptedException {
        myAdapter = new MyAdapter();//适配器，加载内容
        handler = new Handler();
        layoutManager = new LinearLayoutManager(this);

        refreshLayout = findViewById(R.id.swiperefreshlayout);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        //设置下拉时圆圈的颜色
        refreshLayout.setColorSchemeResources( android.R.color.holo_red_light);
        //设置下拉时圆圈的背景颜色
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        //设置下拉刷新时的操作
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getData("refresh");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //加载更多
        mOnLoadMoreListener=new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {//延迟操作
                    @Override
                    public void run() {
                        try {
                            getData("loadMore");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 3000);
            }
        };

        //列表滚动
        recyclerView.addOnScrollListener(mOnLoadMoreListener);

        getData("reset");
    }

    //获取数据，并刷新
    private void getData(final String type) throws InterruptedException {
        if ("reset".equals(type)) {//重置
            productList.clear();
            //获取数据库中推荐标题infoList.get(i).getTitle()，其中infolist由登录页面传入
            for (int i = 0; i < infoList.size(); i++) {
                productList.add(infoList.get(i));
            }
        } else if ("refresh".equals(type)) {//刷新
            productList.clear();
            productList= recommandProducts();//重新获取推荐的产品
        } else {//加载更多
            loadMoreProducts();
        }

        myAdapter.notifyDataSetChanged();//动态更新

        //在获取数据完成后设置刷新状态为false
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }

        if ("refresh".equals(type)) {    //如果是刷新，显示刷新完毕
            Toast.makeText(getApplicationContext(), "刷新完毕", Toast.LENGTH_SHORT).show();
        } else {                        //如果是加载，显示加载完毕
            Toast.makeText(getApplicationContext(), "加载完毕", Toast.LENGTH_SHORT).show();
        }
    }

    //适配器
    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CONTENT = 0;//正常内容
        private final static int TYPE_FOOTER = 1;//加载View

        @Override
        public int getItemViewType(int position) {
            if (position == productList.size() && mOnLoadMoreListener.isAllScreen()) {
                return TYPE_FOOTER;
            }
            return TYPE_CONTENT;
        }

        //方法重写
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {      //刷新
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main_foot, parent, false);
                return new FootViewHolder(view);
            } else {                            //添加条目
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main_item, parent, false);
                MyViewHolder myViewHolder = new MyViewHolder(view);
                return myViewHolder;
            }
        }

        //抽象方法重写
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final boolean[] r = {false};

            if (getItemViewType(position) == TYPE_FOOTER) {
            } else {
                MyViewHolder viewHolder = (MyViewHolder) holder;

                viewHolder.textView.setText(productList.get(position).getTitle());//加载标题

                //根据用户点赞行为显示点赞图片
                try {
                    if ( checkLike(productList.get(position).getPid()) ) {
                        viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.liked));
                    }else{
                        viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.dislike));
                    }
                    r[0]= checkLike(productList.get(position).getPid());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //点赞行为与特效
                viewHolder.imageView.setOnClickListener(view -> {
                    if(r[0] ){
                        r[0] =false;
                        viewHolder.imageView.setImageResource(R.drawable.dislike);
                        removeLikes(productList.get(position).getPid());
                    }else {
                        r[0] =true;
                        viewHolder.imageView.setImageResource(R.drawable.liked);
                        addLikes(productList.get(position).getPid());
                    }
                    viewHolder.imageView.startAnimation(AnimationUtils.loadAnimation(
                            infoActivity.this, R.anim.scale));
                });
            }
        }

        //抽象方法重写，最后size为6
        @Override
        public int getItemCount() {
            return productList.size() + 1;
        }
    }

    //新建textview条目
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
            imageView = itemView.findViewById(R.id.likeItem);
        }
    }

    //刷新
    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);//刷新旋转按钮
        }
    }
    }