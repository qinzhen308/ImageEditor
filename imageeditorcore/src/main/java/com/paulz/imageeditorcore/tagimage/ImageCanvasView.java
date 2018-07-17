package com.paulz.imageeditorcore.tagimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Paul Z on 2018/7/9.
 * Description:
 */

public class ImageCanvasView extends View {
    ImageEditorView parent;
    Paint paint;

    public ImageCanvasView(Context context) {
        super(context);
        init();
    }

    public ImageCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint=new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(parent==null){
            parent=(ImageEditorView) getParent();
        }
        if(parent.getValidRect().isEmpty()){
            return;
        }
        Bitmap bitmap=parent.getBitmap();
        canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),parent.getValidRect(),paint);
    }

}
