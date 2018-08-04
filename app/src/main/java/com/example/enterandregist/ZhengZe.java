package com.example.enterandregist;

import java.util.regex.Pattern;

/**
 * Created by Lenovo on 2018/8/2.
 */
/**
 * 用正则表达式对邮箱的格式进行判断。
 */
public class ZhengZe {
    public final String REGEX_EMAIL ="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    // public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public boolean isEmail(String email){
        return Pattern.matches(REGEX_EMAIL,email);
    }
}
