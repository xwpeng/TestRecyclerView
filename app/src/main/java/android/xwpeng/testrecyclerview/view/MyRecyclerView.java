package android.xwpeng.testrecyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.Scroller;

/**
 * Created by xwpeng on 17-3-21.
 */

public class MyRecyclerView extends BasicRecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private float mLastX;
    private float mLastY;

    private void init() {
        if (mScroller == null) {
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        boolean intercept;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx =  Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);
                Log.e("xwpeng16", "dx : " + dx + " dy: " + dy);
                if (dx == 0) {
                    intercept = true;
                } else {
                    intercept =  (dy / dx) > 1;
                    Log.e("xwpeng16", "dy/dx :" + dy/dx);
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                intercept = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        //这里注意要这么写
        return intercept ? intercept : super.onInterceptTouchEvent(e);
    }
}
