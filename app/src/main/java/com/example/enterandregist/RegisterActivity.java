package com.example.enterandregist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lenovo on 2018/7/20.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextP, editTextCT,editSMS;//手机号、密码、验证码
    private Button button,SMSBtn;//注册按钮、获取验证码的按钮
    private ImageView returnImage;//返回按钮
    private TextView enterText;//登录按钮
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
        button =(Button) findViewById(R.id.btn_now_register);
        enterText =(TextView) findViewById(R.id.tv_enter);
        returnImage= (ImageView) findViewById(R.id.iv_return);
        SMSBtn =(Button) findViewById(R.id.bn_sms_code);
        button.setOnClickListener(this);
        SMSBtn.setOnClickListener(this);
        returnImage.setOnClickListener(this);
        enterText.setOnClickListener( this);
    }

    /**
     * 点击事件
     */
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
            case R.id.bn_sms_code://获取验证码
                String phone=editTextP.getText().toString().trim();
                if(phone.length()!=11){
                    Toast.makeText(this, "电话号码不合理", Toast.LENGTH_SHORT).show();
                }else{  //如果电话号码正常的话就提示用户验证码获取成功。
                    Toast.makeText(this, "验证码获取成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 注册方法。
     */
    private void register() {
        String username=editTextP.getText().toString().trim();//获取电话号码
        String password=editSMS.getText().toString().trim();//获取验证码
        String confirmpassword=editTextCT.getText().toString().trim();//获取设置的密码。
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();//使输入框失去焦点
            return;
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            editSMS.requestFocus();//使输入框失去焦点
            return;
        }else if(TextUtils.isEmpty(confirmpassword)){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog pd=new ProgressDialog(this);//初始化等待条
        pd.setMessage("正在注册...");//显示等待条的信息
        pd.show();//显示等待条

        /**
         * 新建一个线程
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                pd.dismiss();//等待条消失
                returnEnter();//从注册界面跳转到登陆界面
            }
        }).start();//开启线程
    }

    /**
     * 跳转到登陆界面
     */
    private void returnEnter() {
        Intent intent=new Intent(this,EnterActivity.class);
        startActivity(intent);
    }
}

