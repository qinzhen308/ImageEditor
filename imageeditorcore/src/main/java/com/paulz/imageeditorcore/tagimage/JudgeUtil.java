package com.paulz.imageeditorcore.tagimage;

import android.graphics.RectF;

/**
 * Created by Paul Z on 2018/7/9.
 * Description:
 */

public class JudgeUtil {

    public final static int TOUCH_SCOPE=5;
    public static RectF temp=new RectF();

    public static boolean contain(float x,float y,IDecor decor){
        temp.set(x-TOUCH_SCOPE,y-TOUCH_SCOPE,x+TOUCH_SCOPE,y+TOUCH_SCOPE);
        return decor.getRect().intersect(temp);
    }
}
