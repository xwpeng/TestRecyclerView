package android.xwpeng.testrecyclerview.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.xwpeng.testrecyclerview.R;
import android.xwpeng.testrecyclerview.util.PublicUtil;

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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_int_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder vh = (ViewHolder) holder;
        final int data = mData.get(position);
        vh.contentView.setText(data + "");
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicUtil.showToast("click: " + data);
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
        private TextView contentView;
        private TextView deleteView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            contentView = (TextView) itemView.findViewById(R.id.item_int_list_textview);
            deleteView = (TextView) itemView.findViewById(R.id.item_int_list_deleteview);
        }
    }

    public interface CallBack{
        void deleteItem(int position);
    }

}
