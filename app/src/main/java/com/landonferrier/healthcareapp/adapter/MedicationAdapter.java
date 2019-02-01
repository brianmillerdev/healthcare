package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.models.TaskModel;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        public  CustomFontTextView tvName;
        public  CustomFontTextView tvFrequency;

        private CustomFontTextView deleteLayout;


        public SimpleViewHolder(View view) {
            super(view);
            tvName = (CustomFontTextView) view.findViewById(R.id.tv_name_amount);
            tvFrequency = (CustomFontTextView) view.findViewById(R.id.tv_frequency);
            deleteLayout = (CustomFontTextView) view.findViewById(R.id.btn_delete);
        }

        public void onBindeViewHolder(ParseObject model, OnItemSelectedListener listener) {

            int amount = model.getInt("amount");
            String name = model.getString("name");
            Date date = model.getDate("date");

            JSONArray times = model.getJSONArray("times");
            String string = "";
            int num = 0;
            for (int i = 0; i < times.length(); i++) {
                try {
                    String time = times.getString(i);
                    if (i == 0) {
                        string = time;
                    }else{
                        string = String.format("%s, %s", string, time);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            tvName.setText(String.format("%s, %smg", name, amount));
            tvFrequency.setText(string);
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) {
                        listener.onDelete(model, getAdapterPosition());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) {
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

    public MedicationAdapter(Context context) {
        mContext = context;
    }

    public MedicationAdapter(Context context, ArrayList<ParseObject> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public MedicationAdapter(Context context, ArrayList<ParseObject> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_medication, parent, false);
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
