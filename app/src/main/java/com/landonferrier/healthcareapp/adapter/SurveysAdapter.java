package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SurveysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static Context mContext;
    private ArrayList<ParseObject> mItems = new ArrayList<>();
    private int mCurrentItemId = 0;
    private OnItemSelectedListener listener;
    public static String surveyDoctorClassName = "SurveyDoctor";
    public static String surveyStayClassName = "SurveyHospital";
    public static String formattedNameStay = "Hospital Stay Survey";
    public static String formattedNameDoctor = "Doctor's Appointment Survey";

    public void setmItems(ArrayList<ParseObject> models) {
        this.mItems = models;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener {

        void onSelect(ParseObject object, int position);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public  CustomFontTextView tvName;
        public  CustomFontTextView tvFrequency;

        public SimpleViewHolder(View view) {
            super(view);
            tvName = (CustomFontTextView) view.findViewById(R.id.tv_name);
            tvFrequency = (CustomFontTextView) view.findViewById(R.id.tv_answer);
        }

        public void onBindeViewHolder(ParseObject model, OnItemSelectedListener listener) {

            String name = model.getClassName();
            if (name.equals(surveyDoctorClassName)){
                tvName.setText(formattedNameDoctor);
            }else if (name.equals(surveyStayClassName)) {
                tvName.setText(formattedNameStay);
            }
            Date date = model.getDate("expirationDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y");
            tvFrequency.setText(String.format("Please answer by %s", dateFormat.format(date)));
            if (getAdapterPosition() % 2 == 0) {
                itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
            }
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

    public SurveysAdapter(Context context) {
        mContext = context;
    }

    public SurveysAdapter(Context context, ArrayList<ParseObject> messageModels, OnItemSelectedListener listener) {
        this.mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public SurveysAdapter(Context context, ArrayList<ParseObject> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_survey, parent, false);
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
