package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SurgeryTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static Context mContext;
    private ArrayList<ParseObject> mItems = new ArrayList<>();
    private static ParseObject selectedSurgery;
    private OnItemSelectedListener listener;
    private static String type = "";

    public void setmItems(ArrayList<ParseObject> models) {
        this.mItems = models;
        notifyDataSetChanged();
    }

    public ParseObject getSelectedSurgery() {
        return selectedSurgery;
    }

    public void setSelectedSurgery(ParseObject selectedSurgery) {
        this.selectedSurgery = selectedSurgery;
        notifyDataSetChanged();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public interface OnItemSelectedListener {

        void onSelect(ParseObject object, int position);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public  CustomFontTextView tvTitle;

        public ImageView btnDelete, btnCheck;


        public SimpleViewHolder(View view) {
            super(view);
            tvTitle = (CustomFontTextView) view.findViewById(R.id.tv_surgery_name);
        }

        public void onBindeViewHolder(ParseObject model, OnItemSelectedListener listener) {
            tvTitle.setText(model.getString("name"));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelect(model, getAdapterPosition());
                }
            });
        }

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public SurgeryTypeAdapter(Context context) {
        mContext = context;
    }

    public SurgeryTypeAdapter(Context context, ArrayList<ParseObject> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public SurgeryTypeAdapter(Context context, ArrayList<ParseObject> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_surgery_type, parent, false);
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
