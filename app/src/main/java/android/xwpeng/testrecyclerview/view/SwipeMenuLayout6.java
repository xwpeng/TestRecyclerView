package android.xwpeng.testrecyclerview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.xwpeng.testrecyclerview.R;

/**
 * Created by xwpeng on 17-3-22.
 */
public class SwipeMenuLayout6 extends LinearLayout {
    private static final String TAG = "SwipeMenuLayout5";

    private int mScaleTouchSlop;//为了处理单击事件的冲突
    private int mMaxVelocity;//计算滑动速度用
    private int mPointerId;//多点触摸只算第一根手指的速度
    private int mHeight;//自己的高度
    //右侧菜单宽度总和(最大滑动距离)
    private int mRightMenuWidths;

    //滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
    private int mLimit;

    private View mContentView;

    //private Scroller mScroller;//以前item的滑动动画靠它做，现在用属性动画做
    //上一次的xy
    private PointF mLastP = new PointF();
    //增加一个布尔值变量，dispatch函数里，每次down时，为true，move时判断，如果是滑动动作，设为false。
    //在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
    private boolean isUnMoved = true;

    //判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
    //up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件
    private PointF mFirstP = new PointF();
    private boolean isUserSwiped;

    //存储的是当前正在展开的View
    private static SwipeMenuLayout6 mViewCache;

    //防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
    private static boolean isTouching;

    private VelocityTracker mVelocityTracker;//速度追踪器

    private boolean isSwipeEnable = true;
    private boolean isLeftSwipe = true;

    private boolean isIos = true;
    private boolean iosInterceptFlag;//IOS类型下，是否拦截事件的flag
    private Scroller mScroller;


    public SwipeMenuLayout6(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout6(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }


    public boolean isIos() {
        return isIos;
    }

    public SwipeMenuLayout6 setIos(boolean ios) {
        isIos = ios;
        return this;
    }

    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    public SwipeMenuLayout6 setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
        return this;
    }

    /**
     * 返回ViewCache
     *
     * @return
     */
    public static SwipeMenuLayout6 getViewCache() {
        return mViewCache;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();//大于此值时，计算速率都用此值
        //初始化滑动帮助类对象
        mScroller = new Scroller(context);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            //如果引用成AndroidLib 资源都不是常量，无法使用switch case
            if (attr == R.styleable.SwipeMenuLayout_swipeEnable) {
                isSwipeEnable = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_ios) {
                isIos = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_leftOrRight) {
                isLeftSwipe = ta.getBoolean(attr, true);
            }
        }
        ta.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
         return swipeDispatch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean swipeDispatch(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUserSwiped = false;
                isUnMoved = true;
                iosInterceptFlag = false;
                if (isTouching) {
                    return false;
                } else {
                    isTouching = true;
                }
                mLastP.set(ev.getRawX(), ev.getRawY());
                mFirstP.set(ev.getRawX(), ev.getRawY());
                if (mViewCache != null) {
                    if (mViewCache != this) {
                        mViewCache.smoothClose();
                        iosInterceptFlag = isIos;
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mPointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (iosInterceptFlag) {
                    break;
                }
                float gap = mLastP.x - ev.getRawX();
                if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (Math.abs(gap) > mScaleTouchSlop) {
                    isUnMoved = false;
                }
/*                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }*/
                scrollBy((int) (gap), 0);
                if (isLeftSwipe) {
                    if (getScrollX() < 0) {
                        scrollTo(0, 0);
                    }
                    if (getScrollX() > mRightMenuWidths) {
                        scrollTo(mRightMenuWidths, 0);
                    }
                } else {
                    if (getScrollX() < -mRightMenuWidths) {
                        scrollTo(-mRightMenuWidths, 0);
                    }
                    if (getScrollX() > 0) {
                        scrollTo(0, 0);
                    }
                }
                mLastP.set(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                acquireVelocityTracker(ev);
                final VelocityTracker verTracker = mVelocityTracker;
                if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                    isUserSwiped = true;
                }
                if (!iosInterceptFlag) {
                    verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    final float velocityX = verTracker.getXVelocity(mPointerId);
                    if (Math.abs(velocityX) > 1000) {
                        if (velocityX < -1000) {
                            //左滑
                            if (isLeftSwipe) {
                                smoothExpand();
                            } else {
                                smoothClose();
                            }
                        } else {
                            //右滑
                            if (isLeftSwipe) {
                                smoothClose();
                            } else {
                                smoothExpand();
                            }
                        }
                    } else {
                        if (Math.abs(getScrollX()) > mLimit) {
                            smoothExpand();
                        } else {
                            smoothClose();
                        }
                    }
                }
                releaseVelocityTracker();
                isTouching = false;
                break;
        }
        return  false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) return true;
                case MotionEvent.ACTION_UP:
                    if (isLeftSwipe) {
                        if (getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() < getWidth() - getScrollX()) {
                                if (isUnMoved) smoothClose();
                                return true;
                            }
                        }
                    } else {
                        if (-getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() > -getScrollX()) {
                                if (isUnMoved) smoothClose();
                                return true;
                            }
                        }
                    }
                    if (isUserSwiped) return true;
                    break;
            }
            if (iosInterceptFlag) return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 平滑展开
     */
    private ValueAnimator mExpandAnim, mCloseAnim;

    private boolean isExpand;

    public void smoothExpand() {
        //Log.d(TAG, "smoothExpand() called" + this);
        /*mScroller.startScroll(getScrollX(), 0, mRightMenuWidths - getScrollX(), 0);
        invalidate();*/
        //展开就加入ViewCache：
        mViewCache = SwipeMenuLayout6.this;

        //2016 11 13 add 侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }

        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }

    /**
     * 平滑关闭
     */
    public void smoothClose() {
        //Log.d(TAG, "smoothClose() called" + this);
/*        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        invalidate();*/
        mViewCache = null;

        //侧滑菜单展开，屏蔽content长按
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

   //侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    //展开时，禁止长按
    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void quickClose() {
        if (this == mViewCache) {
            cancelAnim();
            mViewCache.scrollTo(0, 0);//关闭
            mViewCache = null;
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mRightMenuWidths = 0;
        int childSize = getChildCount();
        for (int i = 1; i < childSize; i++) {
            View childV = getChildAt(i);
            if (childV != null && childV.getVisibility() != GONE) {
                mRightMenuWidths += childV.getWidth();
            }
        }
        mLimit = mRightMenuWidths * 4 / 10;
    }
}