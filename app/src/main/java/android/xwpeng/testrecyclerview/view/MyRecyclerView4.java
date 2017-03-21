package android.xwpeng.testrecyclerview.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import static android.R.attr.startX;
import static android.R.attr.startY;

/**
 * Created by xwpeng on 17-3-21.
 */

public class MyRecyclerView4 extends BasicRecyclerView {

    private float mLastX;
    private float mLastY;
    private int touchSlop;
    private boolean isChildHandle;
    private View touchView;
    private Scroller mScroller;

    public MyRecyclerView4(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    public MyRecyclerView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    public MyRecyclerView4(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            isChildHandle = false;
            // 记录手指按下的位置
            mLastX = ev.getY();
            mLastY = ev.getX();
            // 获取按下的那个View
            int position = pointToPosition((int) startX, (int) startY);
            touchView = getChildAt(position);

            if (hasChildOpen()) {
                // 如果触摸的不是打开的那个View, 关闭所有View，并且拦截所有事件
                if (touchView != null && touchView instanceof SwipeMenuLayout4 && ((SwipeMenuLayout4) touchView).isOpen()) {
                    isChildHandle = true; // 将事件交给child！
                } else {
                    closeAllSwipeItem();
                    return false;
                }
            }
        }
        // 禁用多点触控
        if (action == MotionEvent.ACTION_POINTER_DOWN) {
            return false;
        }

        return super.dispatchTouchEvent(ev);
    }

    // 处理和侧滑菜单冲突
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        float x = ev.getX();
        float y = ev.getY();
        float dx = Math.abs(x - mLastX);
        float dy = Math.abs(y - mLastY);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                   intercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isChildHandle) {
                    intercept = false;
                    break;
                }
                if (dx > touchSlop && dx > dy) {
                    isChildHandle = true;
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchView != null && touchView instanceof SwipeMenuLayout4) {
                    SwipeMenuLayout4 swipeItem = (SwipeMenuLayout4) this.touchView;
                    if (swipeItem.isOpen()) {
                        if(dx < touchSlop && dy < touchSlop) {
                            swipeItem.close();
                        }
                        Rect rect = swipeItem.getMenuRect();
                        // 如果不是点击在菜单上，拦截点击事件。
                        if(!(startX > rect.left && startX < rect.right && startY > touchView.getTop() && startY < touchView.getBottom())) {
                            return true;  // return true，拦截Item点击事件, 但是菜单能接收到。
                        }
                    }
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return intercept ? intercept : super.onInterceptTouchEvent(ev);
    }

    /** * 当前手指位置的position(屏幕上显示的第一个Item为0) */
    private Rect touchFrame;
    private int pointToPosition(int x, int y) {
        Rect frame = touchFrame;
        if (frame == null) {
            touchFrame = new Rect();
            frame = touchFrame;
        }
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() == VISIBLE) {
                child.getHitRect(frame);
                if(frame.contains(x, y)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean hasChildOpen() {
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child != null && child instanceof SwipeMenuLayout4) {
                if (((SwipeMenuLayout4) child).isOpen()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void closeAllSwipeItem() {
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child != null && child instanceof SwipeMenuLayout4) {
                ((SwipeMenuLayout4) child).close();
            }
        }
    }
}