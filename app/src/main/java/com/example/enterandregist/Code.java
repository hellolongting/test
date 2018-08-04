package com.example.enterandregist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;


/**
 * Created by Lenovo on 2018/7/21.
 */

public class Code {
    //验证码的内容,拼装成一个验证码
    private static final char[] CHARS={'0','1','2','3','4','5','6','7','8','9','a',
            'b','c','d','e', 'f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A',
            'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    //验证码图片
    private int width=100;
    private  int height=40;
    private String code;//声明变量。
    private int codeLength=4;//生成的个数.
    private int lineLenght=5;
    private Random random =new Random();

    private int padding_left;
    private int padding_top;

    //当用户点击图片时（即输出为null），重新生成一张验证码的图。
    private static Code bmpCode;
    public static Code getInstance(){
        if(bmpCode==null)
            bmpCode= new Code();
        return bmpCode;
    }

    public Bitmap createBitmap(){//生成
        padding_left=0;
        Bitmap bp=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);//创建一个画布
        Canvas c= new Canvas(bp);//创建画布
        code = createCode();
        Paint paint =new Paint();//创建画笔。
        c.drawColor(Color.WHITE);//设置画布颜色为白色
        paint.setTextSize(20);//设计画笔的大小。
        paint.setAntiAlias(true);//防止边沿有锯齿，
        for(int i=0;i<codeLength;i++)//逐一绘制每个字符
        {
            randomTextStyle(paint);
            randomPadding();//让下面的边界变成随机数。
            c.drawText(code.charAt(i)+"",padding_left,padding_top,paint);//创建文字
        }
        //下面绘制干扰线。
        for(int i=0;i<lineLenght;i++){
            drawLine(c,paint);
        }
        c.save(Canvas.ALL_SAVE_FLAG);//保存
        c.restore();//将画板和画笔等的状态进行保存。方便再次使用。
        return bp;
    }

    //下面的画干扰线的方法：
    private void drawLine(Canvas c, Paint paint) {
        int color=randomColor();//随机生成颜色
        int startX=random.nextInt(width);//所生成的干扰线的其实左边的横坐标早0-weidth之间，即是在画布的范围内。
        int startY=random.nextInt(height);
        int stopX=random.nextInt(width);
        int stopY=random.nextInt(height);
        paint.setStrokeWidth(1);//设置画笔的宽度为1dp;
        paint.setColor(color);//利用刚刚随及生成的颜色设置当前的画笔.
        c.drawLine(startX,startY,stopX,stopY,paint);//在此正式画出了干扰线。
    }

    private void randomPadding() {
        padding_left+=10+random.nextInt(10);//这里的随机数范围时0-10.
        padding_top=15+random.nextInt(15);
    }

    private void randomTextStyle(Paint paint) {
        int color=randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());//随机，true 为粗体，flash为斜体
        float skewX=random.nextInt(11)/10;
        skewX=random.nextBoolean()?skewX:-skewX;
        paint.setTextSkewX(skewX);//负数为右斜，证书为左斜。
    }

    //生成验证码上的字符,返回所生成的验证码。
    private String createCode() {
        StringBuilder buffer =new StringBuilder();
        for(int i=0;i<codeLength;i++){
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    //下面随机生成颜色。因为所有譅都以红绿蓝为底色，所以随机为红绿蓝的混合色。
    private int randomColor(){
        int red=random.nextInt();
        int green=random.nextInt();
        int blue= random.nextInt();
        return Color.rgb(red,green,blue);
    }

    public String getCode() {
        return code;
    }
}
