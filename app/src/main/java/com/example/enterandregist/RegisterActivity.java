package com.example.enterandregist;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by Lenovo on 2018/7/20.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextP, editTextCT,editTextCT1,editSMS;//手机号、密码、确认密码、邮箱
    private Button button;//注册按钮
    private ImageView returnImage;//返回按钮
    private TextView enterText;//登录按钮
    private String result="";//一个代表显示内容的字符串。
    @Override
    protected void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);
        init();
    }

    /**
     * 初始化控件的方法
     */
    private void init() {
        editTextP =(EditText) findViewById(R.id.et_phone_num);
        editSMS =(EditText) findViewById(R.id.et_sms_code);
        editTextCT =(EditText) findViewById(R.id.et_password);
        editTextCT1=(EditText) findViewById(R.id.et_password1);//确认密码项
        button =(Button) findViewById(R.id.btn_now_register);
        enterText =(TextView) findViewById(R.id.tv_enter);
        returnImage= (ImageView) findViewById(R.id.iv_return);
        //SMSBtn =(Button) findViewById(R.id.bn_sms_code);
        button.setOnClickListener(this);
        //SMSBtn.setOnClickListener(this);
        returnImage.setOnClickListener(this);
        enterText.setOnClickListener( this);
    }

    /**
     * 点击事件
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_now_register://注册按钮
                register();//注册方法
                break;
            case R.id.tv_enter://按了登录按钮，要跳转到登陆界面
                returnEnter();//直接回到登陆界面
                break;
            case R.id.iv_return://按了返回按钮
                returnEnter();
                break;

        }
    }

    /**
     * 注册方法。
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void register() {
        final String username=editTextP.getText().toString().trim();//获取电话号码
        final String mail=editSMS.getText().toString().trim();//获取邮箱
        final String password=editTextCT.getText().toString().trim();//获取设置的密码。
        final String password1=editTextCT1.getText().toString().trim();//获取确认密码的内容。
        ZhengZe EMAIL =new ZhengZe();
        if(!(password.equals(password1))){
            Toast.makeText(this, "两次输入的密码有误，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!user(username)){
            Toast.makeText(this, "用户名只能由字母数字汉字或者下划线组成,且长度为2-8", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();//使输入框失去焦点
            return;
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
            editSMS.requestFocus();//使输入框失去焦点
            return;
        }else if(!EMAIL.isEmail(mail)){
            Toast.makeText(this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
            editSMS.requestFocus();//使输入框失去焦点
            return;
        } else if(!passwordJudge(password)){
            Toast.makeText(this, "密码只能由数字字母组成，且长度为6-16", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd=new ProgressDialog(this);//初始化等待条
        pd.setMessage("正在注册...");//显示等待条的信息
        pd.show();//显示等待条
        new Thread(new Runnable(){
            public void run(){
                post(username, password, mail);
                pd.dismiss();//等待条消失
                 if(("2".equals(result))){
                     try{
                         Looper.prepare();
                         Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                         returnEnter();//从注册界面跳转到登陆界面
                         System.out.println();
                         System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111            "+result);
                         Looper.loop();
                     }catch(Exception e){
                     }

                 }else if(("1".equals(result))){
                     try{
                         Looper.prepare();
                         Toast.makeText(RegisterActivity.this, "邮箱已被占用", Toast.LENGTH_SHORT).show();
                         Looper.loop();
                     }catch(Exception e){
                     }
                }else if(("0".equals(result))){
                     try{
                         Looper.prepare();
                         Toast.makeText(RegisterActivity.this, "用户名已被使用", Toast.LENGTH_SHORT).show();
                         Looper.loop();
                     }catch(Exception e){
                     }
                 }
                 System.out.println();
                 System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111            "+result);
            }
        }).start();
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
    private void post(String username, String password, String mail) {
        String target ="http://personweb.top:8080/Demo3/AndroidUserServlet?method=regist";
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
             +"&password="+URLEncoder.encode(String.valueOf(password),"utf-8")
             +"&mail="+URLEncoder.encode(String.valueOf(mail),"utf-8");

            out.writeBytes(param);

            out.flush();//立即刷新。
            out.close();
            while(urlConn.getResponseCode()==HttpURLConnection.HTTP_OK){//对响应码进行判断。
                InputStreamReader in =new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer =new BufferedReader(in);
                String inputLine;
                while((inputLine = buffer.readLine())!=null){
                    result = inputLine;
                }
                in.close();
            }
            urlConn.disconnect();//销毁连接
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void returnEnter() {
        Intent intent=new Intent(this,EnterActivity.class);
        startActivity(intent);
    }
}


