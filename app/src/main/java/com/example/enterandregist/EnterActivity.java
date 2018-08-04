package com.example.enterandregist;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

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

    private String  result="";//一个代表显示内容的字符串。

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_enter);//创建一个布局
        init();//初始化方法,而且所要监听的事件太多了，特意用一个方法，增强可读性。
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
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());//一旦运行就要先获取验证码图片。
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

    private void login() {       //登录的相关操作。
        currentUserName = editPerson.getText().toString().trim();//去除空格，获取手机号/用户名。
        currentPassword = editCode.getText().toString().trim();//去除空格，获取密码。
        String phoneCode = et_phoneCode.getText().toString().toLowerCase();//获取用户输入的验证码

        if(!user(currentUserName)){
            Toast.makeText(this, "用户名只能由字母数字汉字或者下划线组成,且长度为2-8", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordJudge(currentPassword)) {  //判断密码是否为空
            Toast.makeText(this, "密码只能由数字字母组成，且长度为6-16", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断验证码是否输入正确。
        if (TextUtils.isEmpty(phoneCode) || !phoneCode.equals(realCode)) {
            Toast.makeText(this, "验证码错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(EnterActivity.this);//初始化等待动画。
        pd.setMessage("正在登录....");//显示等待信息。
        pd.show();//显示等待条
        /**
         * 后台请求.
         */
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    post(currentUserName, currentPassword);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();//消失等待条。

                if(("1".equals(result))){
                    try{
                        Looper.prepare();
                        Toast.makeText(EnterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EnterActivity.this, MainActivity.class);//跳转主界面
                        startActivity(intent);
                        finish();//销毁当前界面
                        System.out.println();
                        System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111            "+result);
                        Looper.loop();
                    }catch(Exception e){
                    }
                } else if("0".equals(result)){
                    try{
                        Looper.prepare();
                        Toast.makeText(EnterActivity.this, "登录信息错误，请重新登录", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }catch(Exception e){
                    }
            }
                System.out.println();
                System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111            "+result);
        }
            }).start();//开始线程
    }

     private void post(String username, String password) throws JSONException {
     String target ="http://personweb.top:8080/Demo3/AndroidUserServlet?method=login";

     try{
     URL url=new URL(target);
     HttpURLConnection urlConn =(HttpURLConnection) url.openConnection();
     urlConn.setRequestMethod("POST");
     urlConn.setDoInput(true);
     urlConn.setDoOutput(true);
     urlConn.setUseCaches(false);
     urlConn.setInstanceFollowRedirects(true);
     urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
     //超时设计，防止网络异常下可能导致的程序僵死。
     System.setProperty("sun.NET.client.defaultConnectTimeout", "30000");
     System.setProperty("sun.net.client.defaultReadTimeout", "30000");
     //sun.Net.client.defaultConnectTimeout：连接主机的超时时间（单位：毫秒）
     //sun.net.client.defaultReadTimeout：从主机读取数据的超时时间（单位：毫秒）

     DataOutputStream out =new DataOutputStream(urlConn.getOutputStream());
     String param ="username="+ URLEncoder.encode(String.valueOf(username),"utf-8")
     +"&password="+URLEncoder.encode(String.valueOf(password),"utf-8");

     out.writeBytes(param);
     out.flush();
     out.close();
     if(urlConn.getResponseCode()==HttpURLConnection.HTTP_OK){
     InputStreamReader in =new InputStreamReader(urlConn.getInputStream());
     BufferedReader buffer =new BufferedReader(in);
     String inputLine;
     while((inputLine = buffer.readLine())!=null){
     result =inputLine;//接受后台所返回的值：0或者1.
     }
     in.close();
     }
     urlConn.disconnect();
     } catch (ProtocolException e) {
     e.printStackTrace();
     } catch (MalformedURLException e) {
     e.printStackTrace();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }

    public boolean user(String username){
         if(username.length()<2||username.length()>15)
             return false;
        else{
             for(int i=0;i<username.length();i++){
                 char ch=username.charAt(i);
                 int n=(int)ch;
                 //用户名只能由字母数字下划线和汉字组成。
                 if(!(ch>='A'&&ch<='Z'||ch>='a'&&ch<='z'||ch>='0'&&ch<='9'||ch=='_'||(19968 <= n && n <40869)))
                     return false;
             }
             return true;
         }
    }
    public boolean passwordJudge(String password){
        if(password.length()<6||password.length()>16)
            return false;
        else{
            //密码只能是数字字母组成，且长度为6-16.
            for(int i=0;i<password.length();i++){
                char ch=password.charAt(i);
                if(!(ch>='A'&&ch<='Z'||ch>='a'&&ch<='z'||ch>='0'&&ch<='9'))
                    return false;
            }
            return true;
        }
    }
}
