package android.xwpeng.testrecyclerview.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.xwpeng.testrecyclerview.R;
import android.xwpeng.testrecyclerview.entity.ChatMsg;
import android.xwpeng.testrecyclerview.util.PublicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwpeng on 16-11-1.
 * practice listview
 */

public class ListViewActivity extends AppCompatActivity {
    private String[] msgArray = new String[]{"哥，问你个问题", "说吧", "为什么两点之间线段最短？", "你养过狗么？",
            "曾经养过", "你丢一块骨头出去，你说狗是绕个圈去捡还是直接跑过去捡呢？”",
            "当然是直接跑过去捡了！", "连狗都知道的问题你还问？"};
    private boolean[] isMyselfs = new boolean[]{true, false, true, false, true, false, true, false};
    private List<ChatMsg> mData;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        setTitle("ListViewActivity");
        mListView = (ListView) findViewById(R.id.listview);
        initData();
        mListView.setAdapter(new MyAdapter());
    }

    private void initData() {
        int length = msgArray.length;
        mData = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.id = (i + 1);
            chatMsg.msg = msgArray[i];
            chatMsg.isSelf = isMyselfs[i];
            mData.add(chatMsg);
        }
    }

    private class MyAdapter extends BaseAdapter {
        //获得列表项总数
        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        //根据位置获得列表项数据
        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        //根据位置，获取列表项id
        @Override
        public long getItemId(int position) {
            return mData.get(position).id;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return mData.get(position).isSelf ? 1 : 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //根据父视图，获取列表项视图.父视图是listView,会自动进行子控件挂载，所以如下面写法。
            Holder holder = null;
            final ChatMsg msg = mData.get(position);
            if (convertView == null) {
                if (getItemViewType(position) == 1) {
                    convertView = getLayoutInflater().inflate(R.layout.chatting_item_left, parent, false);
                } else {
                    convertView = getLayoutInflater().inflate(R.layout.chatting_item_right, parent, false);
                }
                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.message);
                holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
           convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicUtil.showToast(msg.msg);
                }
            });
            holder.textView.setText((getItemViewType(position) == 1 ? "u:" : "pxw: ") + msg.msg);
            holder.textView.setTextColor(Color.RED);
            System.out.println(convertView);
            return convertView;
        }

        private class Holder {
            public TextView textView;
            public ImageView imageView;
        }
    }

}
