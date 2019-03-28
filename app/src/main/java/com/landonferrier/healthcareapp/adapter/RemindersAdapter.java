package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.models.TaskModel;
import com.landonferrier.healthcareapp.views.AutofitTextView;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemindersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private ArrayList<ParseObject> mItems = new ArrayList<>();
    private int mCurrentItemId = 0;
    private OnItemSelectedListener listener;

    public void setmItems(ArrayList<ParseObject> models) {
        this.mItems = models;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener {

        void onDelete(ParseObject object, int position);
        void onSelect(ParseObject object, int position);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public  AutofitTextView tvName;
        public  AutofitTextView tvTime;
        public  AutofitTextView tvDate;

        private CustomFontTextView deleteLayout;


        public SimpleViewHolder(View view) {
            super(view);
            tvName = (AutofitTextView) view.findViewById(R.id.tv_reminder_name);
            tvTime = (AutofitTextView) view.findViewById(R.id.tv_time);
            tvDate = (AutofitTextView) view.findViewById(R.id.tv_date);
            deleteLayout = (CustomFontTextView) view.findViewById(R.id.btn_delete);
        }

        public void onBindeViewHolder(ParseObject model, OnItemSelectedListener listener) {


            Date date = model.getDate("time");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm");

            String timeString = dateFormatter.format(date);
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("a");
            String dayNightString = dateFormatter1.format(date);
            tvTime.setText(String.format("%s %s", timeString, dayNightString));
            SimpleDateFormat dateFormatter2 = new SimpleDateFormat("EEEE, MMMM d");

            String fullDateString = dateFormatter2.format(date);
            tvDate.setText( fullDateString);

            String name = model.getString("name");
            tvName.setText( name);
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDelete(model, getAdapterPosition());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSelect(model, getAdapterPosition());
                    }
                }
            });
        }

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public RemindersAdapter(Context context) {
        mContext = context;
    }

    public RemindersAdapter(Context context, ArrayList<ParseObject> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public RemindersAdapter(Context context, ArrayList<ParseObject> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_reminder, parent, false);
            return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ParseObject model = mItems.get(position);
            SimpleViewHolder holder = (SimpleViewHolder) viewHolder;
            holder.onBindeViewHolder(model, listener);
    }

    public void addItem(int position, ParseObject model) {
        mItems.add(0, model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position );
        notifyItemRangeChanged(position, mItems.size() );
    }

    public void updateItem(int position, ParseObject model) {
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
