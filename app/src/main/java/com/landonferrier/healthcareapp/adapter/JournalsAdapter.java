package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.widget.StickyHeaders;
import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class JournalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaders, StickyHeaders.ViewSetup {

    private static final int HEADER_ITEM = 123;

    Context mcomtext;
    ArrayList<ParseObject> datas = new ArrayList<>();
    private OnItemSelectedListener listener;

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public void setmItems(ArrayList<ParseObject> models) {
        this.datas = models;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener {

        void onDelete(ParseObject object, int position);
        void onSelect(ParseObject object, int position);
    }

    public JournalsAdapter(Context context, ArrayList<ParseObject> datas) {
        this.mcomtext = context;
        this.datas = datas;
    }

    public JournalsAdapter(Context context, ArrayList<ParseObject> datas, OnItemSelectedListener listener) {
        this.mcomtext = context;
        this.datas = datas;
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_ITEM) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_sticky_header, parent, false);
            return new HeaderViewHolder(inflate);
        } else {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
            return new MyViewHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEADER_ITEM) {
            CustomFontTextView textView = holder.itemView.findViewById(R.id.tv_title);
            textView.setText(datas.get(position).getString("name"));
        }else{
            ParseObject item = datas.get(position);
            CustomFontTextView tvJournalName = holder.itemView.findViewById(R.id.tv_journal_name);
            CustomFontTextView tvJournalDetail = holder.itemView.findViewById(R.id.tv_detail);
            CustomFontTextView tvDate = holder.itemView.findViewById(R.id.tv_date);
            CustomFontTextView tvMonth = holder.itemView.findViewById(R.id.tv_month);
            CustomFontTextView btnDelete = holder.itemView.findViewById(R.id.btn_delete);
            tvJournalName.setText(item.getString("name"));
            tvJournalDetail.setText(item.getString("text"));

            Date date = item.getDate("date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("d");
            String dateString = dateFormat.format(date);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
            String month = monthFormat.format(date);
            tvDate.setText(dateString);
            tvMonth.setText(month.toUpperCase());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDelete(item, position);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSelect(item, position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        ParseObject object = datas.get(position);

        return object.get("text") == null ? HEADER_ITEM : super.getItemViewType(position);
    }

    @Override
    public boolean isStickyHeader(int position) {
        return getItemViewType(position) == HEADER_ITEM;
    }

    @Override
    public void setupStickyHeaderView(View stickyHeader) {
        ViewCompat.setElevation(stickyHeader, 10);
    }

    @Override
    public void teardownStickyHeaderView(View stickyHeader) {
        ViewCompat.setElevation(stickyHeader, 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}


