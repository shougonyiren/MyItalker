package net.liuhao.italker.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by hasee on 2019-04-15.
 */
public class SquareLayout  extends FrameLayout{

    public SquareLayout(@NonNull Context context) {
        super(context);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //给父类传递的测量值都为宽度，
        //那么就是基于宽度的正方形了
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);//正方形都是，widthMeasureSpec
    }
}











