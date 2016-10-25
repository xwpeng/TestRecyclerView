package android.xwpeng.testrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xwpeng on 16-10-20.
 */

public class IntListAdapter extends RecyclerView.Adapter {
    private List<Integer> mData;

    public IntListAdapter(List<Integer> data) {
        mData = data;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        final int data = mData.get(position);
        vh.textView.setText(data + "");
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicUtil.showToast(data + "");
            }
        });
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
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = (TextView) itemView.findViewById(R.id.item_int_list_textview);
        }
    }
}
