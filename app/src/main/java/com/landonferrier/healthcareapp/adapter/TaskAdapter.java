package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.models.TaskModel;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static Context mContext;
    private ArrayList<TaskModel> mItems = new ArrayList<>();
    private int mCurrentItemId = 0;
    private OnItemSelectedListener listener;

    public void setmItems(ArrayList<TaskModel> models) {
        this.mItems = models;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener {

        void onEdit(TaskModel object, int position);
        void onDelete(TaskModel object, int position);
        void onSelect(TaskModel object, int position);
    }

    public static class SimpleViewHolder0 extends RecyclerView.ViewHolder {
        public CustomFontTextView tvMessage;
        public  CustomFontTextView tvTitle;



        public SimpleViewHolder0(View view) {
            super(view);
        }

        public void onBindeViewHolder(TaskModel model, OnItemSelectedListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSelect(model, getAdapterPosition());
                }
            });

            tvTitle.setText(model.getName());
        }

    }
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCheck;
        public  CustomFontTextView tvTitle;

        private LinearLayout deleteLayout;
        private LinearLayout editLayout;


        public SimpleViewHolder(View view) {
            super(view);
            tvTitle = (CustomFontTextView) view.findViewById(R.id.tv_surgery_name);
            ivCheck = (ImageView) view.findViewById(R.id.iv_check);
        }

        public void onBindeViewHolder(TaskModel model, OnItemSelectedListener listener) {
            if (model.isCompleted()) {
                ivCheck.setImageResource(R.drawable.icon_checkbox_selected);
            } else {
                ivCheck.setImageResource(R.drawable.icon_checkbox);
                ivCheck.setColorFilter(ContextCompat.getColor(mContext, R.color.colorgray), PorterDuff.Mode.SRC_IN);
                tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorgray));
                if ((model.getDate().getTime() < new Date().getTime()) && (!model.isCompleted())) {
                    tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                    ivCheck.setColorFilter(ContextCompat.getColor(mContext, R.color.colorRed), PorterDuff.Mode.SRC_IN);
                }
            }


            tvTitle.setText(model.getName());
        }

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public TaskAdapter(Context context) {
        mContext = context;
    }

    public TaskAdapter(Context context, ArrayList<TaskModel> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public TaskAdapter(Context context, ArrayList<TaskModel> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false);
            return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TaskModel model = mItems.get(position);
            SimpleViewHolder holder = (SimpleViewHolder) viewHolder;
            holder.onBindeViewHolder(model, listener);
    }

    public void addItem(int position, TaskModel model) {
        mItems.add(0, model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position );
        notifyItemRangeChanged(position, mItems.size() );
    }

    public void updateItem(int position, TaskModel model) {
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
