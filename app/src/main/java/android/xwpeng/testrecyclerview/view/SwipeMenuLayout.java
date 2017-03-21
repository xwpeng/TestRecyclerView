package android.xwpeng.testrecyclerview.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;


/**
 * RecyclerView的左滑菜单
 * Created by xwpeng on 17-3-20.
 */

public class SwipeMenuLayout extends LinearLayout {
    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float mLastX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = mLastX - x;
                Log.e("xwpeng16", "scrollx: " + getScrollX());
                if (dx > 0 && getScrollX() < 540){
                    scrollBy((int)(dx), 0);
                    if (getScrollX() > 540) {
                        scrollTo(540, 0);
                    }
                }
                if (dx < 0 && getScrollX() > 0) {
                    scrollBy((int)(dx), 0) ;
                    if (getScrollX() < 0) {
                        scrollTo(0,0);
                    }
                }
        }
        mLastX = x;
        return true;
    }
}
