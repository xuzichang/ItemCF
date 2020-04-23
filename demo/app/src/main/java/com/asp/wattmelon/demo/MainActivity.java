package com.asp.wattmelon.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button login;
    EditText pwd,uid;
    String password;
    int userid;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(Button)findViewById(R.id.btn_login);
        uid=(EditText)findViewById(R.id.et_uid);//用户id
        pwd=(EditText)findViewById(R.id.et_password);//密码
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userid = Integer.parseInt(uid.getText().toString().trim());//获取用户id
                password = pwd.getText().toString().trim();//获取密码
                u = new User(userid,password);//创建用户对象
                checkLogin(u);//用户登录与生成个性推荐列表
                break;
        }
    }

    //用户登录与生成个性推荐列表，传入用户对象，线程内操作数据库
    private void checkLogin(User user) {
        new Thread() {
            public void run() {
                try {
                    Map<String,String> result= DBUtils.login(user);//用户登录，保存用户信息
                    ArrayList<Product> p = ItemSimilarity.recommend(Integer.valueOf(userid));//传入用户id，针对id进行个性推荐
                    if (result != null && result.size() > 0) {
                        Intent intent=new Intent(getBaseContext(),infoActivity.class);
                        intent.putExtra("user",user);//页面跳转携带参数
                        intent.putExtra("product",p);//页面跳转携带参数
                        getBaseContext().startActivity(intent);//页面跳转
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

}
