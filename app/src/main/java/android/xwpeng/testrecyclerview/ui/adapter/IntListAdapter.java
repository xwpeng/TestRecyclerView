package android.xwpeng.testrecyclerview.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.xwpeng.testrecyclerview.R;
import android.xwpeng.testrecyclerview.util.PublicUtil;
import android.xwpeng.testrecyclerview.view.SwipeMenuLayout5;

import java.util.List;

/**
 * Created by xwpeng on 16-10-20.
 */

public class IntListAdapter extends RecyclerView.Adapter {
    private List<Integer> mData;
    private CallBack mCallBack;

    public IntListAdapter(List<Integer> data, CallBack callBack) {
        mData = data;
        mCallBack = callBack;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_int_list2, parent, false));
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_int_list5, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder vh = (ViewHolder) holder;
        final int data = mData.get(position);
        vh.titleView.setText(data + "");
        vh.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.swipeMenuLayout.setLeftSwipe(true);
                PublicUtil.showToast("click data:" + data);
            }
        });
        vh.contentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PublicUtil.showToast("long click data: " + data);
                vh.swipeMenuLayout.setLeftSwipe(false);
                return true;
            }
        });

        vh.menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicUtil.showToast("menu1 data: " + data);
            }
        });
        vh.menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicUtil.showToast("menu2 data: " + data);
            }
        });
        vh.menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicUtil.showToast("menu3 data: " + data);
            }
        });

  /*      vh.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = vh.getAdapterPosition();
                if (adapterPosition >= 0) {
                    mCallBack.deleteItem(adapterPosition);
                    PublicUtil.showToast("delete: " + position);
                }
            }
        });*/


    }
//
//    /**
//     * @param holder
//     * @param position
//     * @param payloads
//     */
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
//        onBindViewHolder(holder, position);
//    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private View contentView;
        private TextView deleteView;
        private TextView titleView;
        private View menu1, menu2, menu3 ;
        private SwipeMenuLayout5 swipeMenuLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            contentView = itemView.findViewById(R.id.item_content);
            deleteView = (TextView) itemView.findViewById(R.id.item_int_list_deleteview);
            titleView = (TextView) itemView.findViewById(R.id.item_int_list_textview);
            menu1 = itemView.findViewById(R.id.item_menu1);
            menu2 = itemView.findViewById(R.id.item_menu2);
            menu3 = itemView.findViewById(R.id.item_menu3);
            swipeMenuLayout = (SwipeMenuLayout5)itemView.findViewById(R.id.item_swipe_menu_layout);
        }
    }

    public interface CallBack {
        void deleteItem(int position);
    }

}
