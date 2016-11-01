package android.xwpeng.testrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IntListAdapter.CallBack{
    private BasicRecyclerView mRecyclerView;
    private List<Integer> mData;
    private IntListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (BasicRecyclerView) findViewById(R.id.main_recyclerview);
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) mData.add(i);
        mAdapter = new IntListAdapter(mData, this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setDivider();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void deleteItem(int position) {
        mData.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}
