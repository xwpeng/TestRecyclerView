package android.xwpeng.testrecyclerview.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.xwpeng.testrecyclerview.view.BasicRecyclerView;
import android.xwpeng.testrecyclerview.ui.adapter.IntListAdapter;
import android.xwpeng.testrecyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwpeng on 16-11-1.
 * practice recyclerview
 */
public class RecyclerViewActivity extends AppCompatActivity implements IntListAdapter.CallBack {
    private BasicRecyclerView mRecyclerView;
    private List<Integer> mData;
    private IntListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        setTitle("RecyclerViewActivity");
        mRecyclerView = (BasicRecyclerView) findViewById(R.id.recyclerview);
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) mData.add(i);
        mAdapter = new IntListAdapter(mData, this);
        mRecyclerView.setDivider();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void deleteItem(int position) {
        mData.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}
