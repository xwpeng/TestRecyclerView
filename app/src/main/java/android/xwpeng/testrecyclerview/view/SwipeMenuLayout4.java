package android.xwpeng.testrecyclerview.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by xwpeng on 17-3-21.
 */

public class SwipeMenuLayout4 extends LinearLayout {
    private boolean mIsOpen;
    private View mMenuView;
    private float mLastX;

    public SwipeMenuLayout4(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMenuView = getChildAt(1);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeMenuLayout4(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mMenuView = getChildAt(1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = mLastX - x;
                if (dx > 0 && getScrollX() < 540) {
                    scrollBy((int) (dx), 0);
                    if (getScrollX() > 540) {
                       open();
                    }
                }
                if (dx < 0 && getScrollX() > 0) {
                    scrollBy((int) (dx), 0);
                    if (getScrollX() < 0) {
                        close();
                    }
                }
                break;
        }
        mLastX = x;
        return true;
    }


    public void close() {
        scrollTo(0, 0);
        mIsOpen = false;
    }

    public void open() {
        scrollTo(540, 0);
        mIsOpen = true;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    private Rect outRect = new Rect();

    public Rect getMenuRect() {
        mMenuView.getHitRect(outRect);
        return outRect;
    }
}
