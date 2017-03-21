package android.xwpeng.testrecyclerview.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.xwpeng.testrecyclerview.R;

/**
 * Created by xwpeng on 17-3-21.
 */

public class SwipeMenuLayout3 extends LinearLayout {
    private final ViewDragHelper mDragHelper;
    private boolean mIsOpen;
    private int mCurrentState;
    private View mContentView;
    private View mMenuView;
    private int mMenuWidth;

    public SwipeMenuLayout3(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout3(Context context, AttributeSet attrs) {
       this(context, attrs, 0);
    }

    public SwipeMenuLayout3(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwipeMenuLayout3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDragHelper = ViewDragHelper.create(this, rightCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewById(R.id.item_content);
        mMenuView = getChildAt(R.id.item_menu);
        mMenuWidth = 540;
        Log.e("xwpeng", "menuWidth: " + mMenuWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    private ViewDragHelper.Callback rightCallback = new ViewDragHelper.Callback() {

        // 触摸到View的时候就会回调这个方法。
        // return true表示抓取这个View。
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mContentView || child == mMenuView;
        }

        //处理横向拖动
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left > 0
                    ? 0 : left < -mMenuWidth
                    ? -mMenuWidth : left;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            // x轴移动速度大于菜单一半，或者已经移动到菜单的一般之后，展开菜单
            if (mIsOpen) {
                if (xvel > mMenuWidth || -mContentView.getLeft() < mMenuWidth / 2) {
                    close();
                } else {
                    open();
                }
            } else {
                if (-xvel > mMenuWidth || -mContentView.getLeft() > mMenuWidth / 2) {
                    open();
                } else {
                    close();
                }
            }
        }
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int getViewVerticalDragRange(View child) {return 1;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            mCurrentState = state;
        }
    };

    public void close() {
        mDragHelper.smoothSlideViewTo(this, 0, getTop());
        mIsOpen = false;
        invalidate();
    }

    public void open() {
        mDragHelper.smoothSlideViewTo(this, -mMenuWidth, getTop());
        mIsOpen = true;
        invalidate();
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public int getState() {
        return mCurrentState;
    }

    private Rect outRect = new Rect();

    public Rect getMenuRect() {
        mMenuView.getHitRect(outRect);
        return outRect;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
