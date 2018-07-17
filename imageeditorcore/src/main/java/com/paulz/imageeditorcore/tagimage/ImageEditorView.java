package com.paulz.imageeditorcore.tagimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul Z on 2018/7/9.
 * Description:
 */

public class ImageEditorView extends FrameLayout implements GestureDetector.OnGestureListener{
    List<IDecor> decors=new ArrayList<>();
    GestureDetector mGestureDetector;
    OnDecorListener mOnDecorListenr;
    IDecor mSelectedDecor;

    RectF validRect=new RectF();
    Bitmap mBitmap;

    ImageCanvasView mCanvasView;

    private boolean isEditable=false;

    public ImageEditorView(Context context) {
        super(context);
        init();
    }

    public ImageEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mGestureDetector=new GestureDetector(getContext(),this);
        mCanvasView=new ImageCanvasView(getContext());
        addView(mCanvasView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onImageChange();
    }

    private void onImageChange(){
        if(getWidth()>0&&getHeight()>0&&mBitmap!=null){
            float scaleBm=mBitmap.getHeight()/(float) mBitmap.getWidth();
            float scaleView=getHeight()/(float) getWidth();
            if(Float.compare(scaleBm,scaleView)>0){
                //以高为准
                float w=getHeight()/scaleBm;
                validRect.set((getWidth()-w)/2,0,(getWidth()+w)/2,getHeight());
            }else {
                //以宽为准
                float h=getWidth()*scaleBm;
                validRect.set(0,(getHeight()-h)/2,getWidth(),(getHeight()+h)/2);
            }
            mCanvasView.invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isEditable&&mGestureDetector.onTouchEvent(event)){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for(int i=0,size=getChildCount();i<size;i++){
            View child=getChildAt(i);
            if(child instanceof IDecor){
                adjustDecor((IDecor)child);
            }
        }
    }

    private void adjustDecor(IDecor decor){
        RectF rect = decor.getRect();
        if(validRect.left>rect.left){
            decor.setX(validRect.left);
        }else if(validRect.right<rect.right){
            decor.setX(validRect.right-rect.width());
        }

        if(validRect.top>rect.top){
            decor.setY(validRect.top);
        }else if(validRect.bottom<rect.bottom){
            decor.setY(validRect.bottom-rect.height());
        }
    }

    public void addDecor(IDecor decor, float x, float y){
        decors.add(decor);
        if(decor instanceof View){
            addView((View) decor);
        }
        RectF rect=decor.getPreJudgedRect();
        if(rect!=null&&!rect.isEmpty()){
            if(validRect.left>rect.left){
                decor.setX(validRect.left);
            }else if(validRect.right<rect.right){
                decor.setX(validRect.right-rect.width());
            }

            if(validRect.top>rect.top){
                decor.setY(validRect.top);
            }else if(validRect.bottom<rect.bottom){
                decor.setY(validRect.bottom-rect.height());
            }
        }else {
            decor.setX(x);
            decor.setY(y);
        }
    }

    public void addDecor(IDecor decor){
        decors.add(decor);
        if(decor instanceof View){
            addView((View) decor);
        }
    }

    public void addAllDecors(List<IDecor> decors){
        decors.addAll(decors);
        boolean needLayout=false;
        for(int i=0,size=decors.size();i<size;i++){
            IDecor decor=decors.get(i);
            if(decor instanceof View){
                needLayout=true;
                View child=(View) decor;
                addViewInLayout(child,getChildCount(),
                        child.getLayoutParams()!=null?
                                child.getLayoutParams():
                                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        if(needLayout)requestLayout();
    }

    public void removeDecor(IDecor decor){
        decors.remove(decor);
        if(decor instanceof View){
            removeView((View) decor);
        }
    }

    public void clearDecors(){
        boolean needLayout=false;
        for(int i=0,size=getChildCount();i<size;i++){
            View v=getChildAt(i);
            if(v instanceof IDecor){
                needLayout=true;
                removeViewInLayout(v);
            }
        }
        if(needLayout)requestLayout();
    }


    public void setImageBitmap(Bitmap bitmap){
        mBitmap=bitmap;
        onImageChange();
    }
    public Bitmap getBitmap(){
        return mBitmap;
    }

    public void setEditable(boolean isEditable){
        this.isEditable=isEditable;
    }

    public boolean isEditable() {
        return isEditable;
    }


    public RectF getValidRect(){
        return validRect;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        float x=e.getX();
        float y=e.getY();
        mSelectedDecor=null;
        for(int i=decors.size()-1;i>=0;i--){
            IDecor decor=decors.get(i);
            if(JudgeUtil.contain(x,y,decor)){
                mSelectedDecor=decor;
                break;
            }
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mOnDecorListenr==null){
            return false;
        }
        if(mSelectedDecor!=null&&validRect.contains(mSelectedDecor.getRect())){
            mOnDecorListenr.onClick(mSelectedDecor);
            return true;
        }else if(validRect.contains(e.getX(),e.getY())){
            mOnDecorListenr.onAddAction( e.getX(),e.getY());
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mSelectedDecor!=null&&validRect.contains(e2.getX(),e2.getY())){
            RectF rect=mSelectedDecor.getRect();
            if(validRect.contains(rect)){
                rect.offset(-distanceX,-distanceY);
                if(validRect.contains(rect)){
                    mSelectedDecor.transX(-distanceX);
                    mSelectedDecor.transY(-distanceY);
                }else {
                    //边缘判定
                    if(validRect.left>rect.left){
                        mSelectedDecor.setX(validRect.left);
                    }else if(validRect.right<rect.right){
                        mSelectedDecor.setX(validRect.right-rect.width());
                    }

                    if(validRect.top>rect.top){
                        mSelectedDecor.setY(validRect.top);
                    }else if(validRect.bottom<rect.bottom){
                        mSelectedDecor.setY(validRect.bottom-rect.height());
                    }
                }

            }
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if(mSelectedDecor!=null&&mOnDecorListenr!=null){
            mOnDecorListenr.onLongClick(mSelectedDecor);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface OnDecorListener {
        void onAddAction(float x, float y);
        void onClick(IDecor decor);
        void onLongClick(IDecor decor);
    }

    public void setOnDecorListenr(OnDecorListener onDecorListenr) {
        this.mOnDecorListenr = onDecorListenr;
    }


    public Bitmap exportBitmap(){
        setDrawingCacheEnabled(true);
        Bitmap bm=getDrawingCache();
        Bitmap out=Bitmap.createBitmap(bm,(int)validRect.left,(int)validRect.top,(int)validRect.width(),(int)validRect.height());
        bm.recycle();
        setDrawingCacheEnabled(false);
        return out;
    }

    public List<IDecor> getDecors(){
        return decors;
    }

}
