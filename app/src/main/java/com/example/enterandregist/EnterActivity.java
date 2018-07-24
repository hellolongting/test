package com.example.enterandregist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

/**
 * Created by Lenovo on 2018/7/19.
 */

public class EnterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editPerson,editCode;//声明用户名，密码的输入框.
    private TextView textViewR;//在登陆界面跳转到注册的注册按钮
    private Button btn;//登陆界面中的登录按钮
//用于加载输入完成后的用户名和密码。
    private static String currentUserName ;
    private String currentPassword;
    private ImageView qq,weixin,weibo;//登陆界面中的QQ,微信，微博的三方登录按钮。

    private EditText et_phoneCode;//输入验证码的框
    private ImageView iv_showCode;//图片验证码
    private String realCode;//用来装载所获得的验证码的值

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_enter);//创建一个布局
        init();//初始化方法
    }

    private void init(){
        editPerson = (EditText) findViewById(R.id.et_username);
        editCode = (EditText) findViewById(R.id.et_password);
        textViewR =(TextView) findViewById(R.id.tv_register);
        btn = (Button) findViewById(R.id.bn_common_login);
        qq =(ImageView) findViewById(R.id.iv_qq_login);
        weixin =(ImageView)  findViewById(R.id.iv_weixin_login);
        weibo = (ImageView) findViewById(R.id.iv_weibo_login);
        btn.setOnClickListener((View.OnClickListener) this);
        qq.setOnClickListener((View.OnClickListener) this);//点击事件的按钮
        weixin.setOnClickListener((View.OnClickListener) this);
        weibo.setOnClickListener((View.OnClickListener) this);
        textViewR.setOnClickListener((View.OnClickListener) this);

        iv_showCode=(ImageView) findViewById(R.id.iv_showCodes);
        et_phoneCode=(EditText) findViewById(R.id.et_phoneCodes);
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());//以运行就要先获取验证码图片。
        realCode=Code.getInstance().getCode().toLowerCase();//将字符全部转换为小写，以方便后面的对比。因为验证码不区分大小写。
        iv_showCode.setOnClickListener(this);//添加验证码的点击事件。

    }

    /**
     *点击事件
     **/
     public void onClick (View view){
        switch(view.getId()){
            case R.id.bn_common_login://登录按钮
                login();//登录方法
                break;
            case R.id.tv_register://注册按钮
                /**
                 * 跳转到注册界面
                 */
                Intent intent=new Intent(EnterActivity.this,RegisterActivity.class);//跳转到注册界面
                startActivity(intent);
                finish();//销毁当前界面。
                break;
            case R.id.iv_qq_login://QQ登录
                Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show();
                login1();
                break;
            case R.id.iv_weixin_login://微信登录
                Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show();
                login1();
                break;
            case R.id.iv_weibo_login://微博登录
                Toast.makeText(this, "微博登录", Toast.LENGTH_SHORT).show();
                login1();
                break;
            case R.id.iv_showCodes://验证码的点击事件
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());//设置新的验证码
                realCode=Code.getInstance().getCode().toLowerCase();//因为换了验证码，所以这里要换原来存储起来的验证码的值。
                break;
        }
    }

    private void login1() {
        final ProgressDialog pd=new ProgressDialog(EnterActivity.this);//初始化等待动画。
        pd.setMessage("正在登录....");//显示等待信息。
        pd.show();//显示等待条

        /**
         * 模拟后台请求.
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);//等待2秒,模拟后台运行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();//消失等待条。
                /**
                 * 跳转到主界面
                 */
                Intent intent = new Intent(EnterActivity.this, MainActivity.class);//跳转主界面
                startActivity(intent);
                finish();//销毁当前界面。
            }
        }).start();//开始线程
    }

    private void login(){       //登录的相关操作。
        currentUserName=editPerson.getText().toString().trim();//去除空格，获取手机号/用户名。
        currentPassword=editCode.getText().toString().trim();//去除空格，获取密码。
        String phoneCode= et_phoneCode.getText().toString().toLowerCase();//获取用户输入的验证码

        if(TextUtils.isEmpty(currentUserName)){  //判断用户名是不是为空。
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(currentPassword)){  //判断密码是否为空
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断验证码是否输入正确。
        if(TextUtils.isEmpty(phoneCode)||!phoneCode.equals(realCode)){
            Toast.makeText(this, "验证码错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd=new ProgressDialog(EnterActivity.this);//初始化等待动画。
        pd.setMessage("正在登录....");//显示等待信息。
        pd.show();//显示等待条

        /**
         * 模拟后台请求.
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);//等待2秒,模拟后台运行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();//消失等待条。
                /**
                 * 跳转到主界面
                 */
                Intent intent = new Intent(EnterActivity.this, MainActivity.class);//跳转主界面
                startActivity(intent);
                finish();//销毁当前界面。
            }
        }).start();//开始线程
    }
}
