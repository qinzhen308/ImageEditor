package com.paulz.imageeditorcore.tagimage;


import android.graphics.RectF;

/**
 * Created by Paul Z on 2018/7/9.
 * Description:
 */

public interface IDecor {

    public void setX(float x);
    public void setY(float y);

    public float getX();
    public float getY();

    public void transX(float dx);
    public void transY(float dy);


    public RectF getRect();
    public RectF getPreJudgedRect();

}
