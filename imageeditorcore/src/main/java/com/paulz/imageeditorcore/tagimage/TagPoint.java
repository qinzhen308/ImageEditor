package com.paulz.imageeditorcore.tagimage;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paulz.imageeditorcore.R;


/**
 * Created by Paul Z on 2018/7/9.
 * Description:
 */

public class TagPoint extends LinearLayout implements IDecor{
    TextView textView;
    View vTagLeft;
    View vTagRight;
    int[] bg={R.drawable.bg_tag_point_left,R.drawable.bg_tag_point_right};
    public static final int MODE_RIGHT=0;
    public static final int MODE_LEFT=1;
    int mode=MODE_RIGHT;

//    TagPointModel model;

    public TagPoint(Context context) {
        super(context);
        init();
    }

    public TagPoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagPoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tag_point,this);
        textView=findViewById(R.id.tv_point_content);
        vTagLeft=findViewById(R.id.v_point_left);
        vTagRight=findViewById(R.id.v_point_right);
    }

    @Override
    public void setX(float x) {

        super.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public void transX(float dx) {
        setX(getX()+dx);
    }

    @Override
    public void transY(float dy) {
        setY(getY()+dy);
    }

    @Override
    public RectF getRect() {
        return new RectF(getX(),getY(),getX()+getWidth(),getY()+getHeight());
    }

    @Override
    public RectF getPreJudgedRect() {
        return null;
    }

//    public void setData(TagPointModel model){
//        this.model=model;
//        textView.setText(model.name);
//    }

    public String getText(){
        return textView.getText().toString();
    }


    public void setMode(int mode){
        this.mode=mode;
        textView.setBackgroundResource(bg[mode]);
        vTagLeft.setVisibility(mode==MODE_RIGHT?VISIBLE:GONE);
        vTagRight.setVisibility(mode==MODE_LEFT?VISIBLE:GONE);
    }

    public void toggleMode(){
        setMode(mode==MODE_LEFT?MODE_RIGHT:MODE_LEFT);
    }

//    public TagPointModel getData(){
//        return model;
//    }
//
//    public TagPointModel getHandledData(){
//        TagPointModel handled=new TagPointModel();
//        handled.name=model.name;
//        handled.id =model.id;
//        handled.type=model.type;
//        handled.x=getX();
//        handled.y=getY();
//        return handled;
//    }

}
