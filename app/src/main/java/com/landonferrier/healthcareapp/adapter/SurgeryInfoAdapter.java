package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.models.TaskModel;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurgeryInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private ArrayList<String> mItems = new ArrayList<>();
    private int mCurrentItemId = 0;
    private OnItemSelectedListener listener;
    private ParseObject surgery;
    public void setmItems(ArrayList<String> models) {
        this.mItems = models;
        notifyDataSetChanged();
    }

    public void setSurgery(ParseObject surgery) {
        this.surgery = surgery;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener {

        void onSelect(int position);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public  CustomFontTextView tvName;

        private LinearLayout deleteLayout;
        private LinearLayout editLayout;


        public SimpleViewHolder(View view) {
            super(view);
            tvName = (CustomFontTextView) view.findViewById(R.id.tv_item_name);
        }

        public void onBindeViewHolder(String model, OnItemSelectedListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSelect(getAdapterPosition());
                }
            });
            tvName.setText( model);
        }

    }


    public SurgeryInfoAdapter(Context context) {
        mContext = context;
    }

    public SurgeryInfoAdapter(Context context, ArrayList<String> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public SurgeryInfoAdapter(Context context, ArrayList<String> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_info, parent, false);
            return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        String model = mItems.get(position);
            SimpleViewHolder holder = (SimpleViewHolder) viewHolder;
            holder.onBindeViewHolder(model, listener);
    }

    public void addItem(int position, String model) {
        mItems.add(0, model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position );
        notifyItemRangeChanged(position, mItems.size() );
    }

    public void updateItem(int position, String model) {
        mItems.set(position, model);
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
            return 0;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
