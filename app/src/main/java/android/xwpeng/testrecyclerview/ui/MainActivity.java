package android.xwpeng.testrecyclerview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.xwpeng.testrecyclerview.R;

/**
 * Created by xwpeng on 16-11-1.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private View mLinearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");
        findViewById(R.id.main_recyclerview_enter).setOnClickListener(this);
        findViewById(R.id.main_listview_enter).setOnClickListener(this);
        mLinearLayout = findViewById(R.id.test_linearLayout);
        mLinearLayout.setOnClickListener(this);
        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            private float mLastX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                       float distance = x - mLastX;
                        v.scrollBy((int) -distance, 0);
                        break;
                }
                mLastX = x;
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_recyclerview_enter:
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                break;
            case R.id.main_listview_enter:
                startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                break;
            case R.id.test_linearLayout:
                break;
        }
    }
}
