package android.xwpeng.testrecyclerview.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


/**
 * RecyclerView的左滑菜单
 * Created by xwpeng on 17-3-20.
 */

public class SwipeMenuLayout extends LinearLayout {
/*    private View mContentView;
    private LinearLayout mMenuLayout;

    public void  setContentView (View contentView) {
        this.mContentView = contentView;
        mContentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    public void setMenuLayout(LinearLayout menuLayout) {
        this.mMenuLayout = menuLayout;
        mMenuLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }*/

    public SwipeMenuLayout(Context context) {
        super(context);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float mLastX;
    private float mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - mLastX) > Math.abs(y -mLastY)) {
                    //滑动内容
                    scrollBy((int)(mLastX - x), 0);
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(event);
    }
}
