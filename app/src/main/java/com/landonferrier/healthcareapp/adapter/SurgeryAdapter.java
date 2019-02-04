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
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SurgeryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

        void onEdit(ParseObject object, int position);
        void onDelete(ParseObject object, int position);
        void onSelect(ParseObject object, int position);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCheck;
        public  CustomFontTextView tvTitle;
        public  CustomFontTextView tvDate;

        public ImageView btnDelete, btnCheck;
        private LinearLayout deleteLayout;
        private LinearLayout editLayout;


        public SimpleViewHolder(View view) {
            super(view);
            tvTitle = (CustomFontTextView) view.findViewById(R.id.tv_surgery_name);
            tvDate = (CustomFontTextView) view.findViewById(R.id.tv_surgery_date);
            ivCheck = (ImageView) view.findViewById(R.id.iv_check);
            if (type.equals("change") || type.equals("remove")) {
                btnDelete = (ImageView) view.findViewById(R.id.btn_delete);
                btnCheck = (ImageView) view.findViewById(R.id.btn_check);
                if (type.equals("change")) {
                    btnDelete.setVisibility(View.GONE);
                    btnCheck.setVisibility(View.INVISIBLE);
                }else if (type.equals("remove")){
                    btnCheck.setVisibility(View.INVISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }
        }

        public void onBindeViewHolder(ParseObject model, OnItemSelectedListener listener) {
            tvTitle.setText(model.getString("name"));
            if (selectedSurgery != null) {
                if (selectedSurgery.getObjectId().equals(model.getObjectId())){
                    if (type.equals("new")) {
                        ivCheck.setImageResource(R.drawable.icon_checkbox_selected);
                        tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    }else if (type.equals("change")){
                        btnCheck.setVisibility(View.VISIBLE);
                    }
                }else{
                    if (type.equals("new")) {
                        ivCheck.setImageResource(R.drawable.icon_checkbox);
                        tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorText));
                    }else if (type.equals("change")){
                        btnCheck.setVisibility(View.INVISIBLE);
                    }
                }
            }else{
                if (type.equals("new")) {
                    ivCheck.setImageResource(R.drawable.icon_checkbox);
                    tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorText));
                }else if (type.equals("change")){
                    btnCheck.setVisibility(View.INVISIBLE);
                }
            }
            if (type.equals("new")) {
                tvDate.setVisibility(View.GONE);
            }else{
                JSONObject surgeryDates = ParseUser.getCurrentUser().getJSONObject("surgeryDates");
                try {
                    if (surgeryDates.get(model.getObjectId()) != null) {
                        String unwrappedDateString = surgeryDates.getString(model.getObjectId());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = simpleDateFormat.parse(unwrappedDateString);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                        String dateString = dateFormat.format(date);
                        tvDate.setText(String.format("Scheduled for %s", dateString));
                        tvDate.setVisibility(View.VISIBLE);
                    }else{
                        tvDate.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvDate.setVisibility(View.GONE);
                } catch (ParseException e) {
                    tvDate.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }
            if (type.equals("remove")) {
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onDelete(model, getAdapterPosition());
                        }
                    }
                });
            }else{
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

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public SurgeryAdapter(Context context) {
        mContext = context;
    }

    public SurgeryAdapter(Context context, ArrayList<ParseObject> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public SurgeryAdapter(Context context, ArrayList<ParseObject> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (type.equals("new")) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_surgery, parent, false);
            return new SimpleViewHolder(view);
        }else{
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_change_remove_surgery, parent, false);
            return new SimpleViewHolder(view);
        }
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
