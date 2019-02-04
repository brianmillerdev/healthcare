package com.landonferrier.healthcareapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.landonferrier.healthcareapp.R;
import com.landonferrier.healthcareapp.utils.Utils;
import com.landonferrier.healthcareapp.views.CustomFontTextView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static Context mContext;
    private ArrayList<Uri> mItems = new ArrayList<>();
    private int mCurrentItemId = 0;
    private OnItemSelectedListener listener;
    private ParseObject surgery;
    public void setmItems(ArrayList<Uri> models) {
        this.mItems = models;
        notifyDataSetChanged();
    }

    public void setSurgery(ParseObject surgery) {
        this.surgery = surgery;
        notifyDataSetChanged();
    }

    public interface OnItemSelectedListener {

        void onSelect(int position);
        void onDelete(int position);
        void onAddPhoto();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvPhoto;
        public ImageView btnDelete;
        public CardView cardView;

        private LinearLayout deleteLayout;
        private LinearLayout editLayout;


        public SimpleViewHolder(View view) {
            super(view);
            imvPhoto = (ImageView) view.findViewById(R.id.imv_photo);
            btnDelete = (ImageView) view.findViewById(R.id.btn_delete);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }

        public void onBindeViewHolder(Uri model, OnItemSelectedListener listener) {

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null)
//                        listener.onSelect(getAdapterPosition());
//                }
//            });
            Picasso.get().load(model).resize(Utils.convertDpToPx(mContext,120), Utils.convertDpToPx(mContext,120)).into(imvPhoto);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDelete(getAdapterPosition());
                }
            });
        }

    }


    public static class SimpleViewHolder1 extends RecyclerView.ViewHolder {
        public ImageView imvPhoto;
        public CardView cardView;
        public ImageView btnDelete;

        private LinearLayout deleteLayout;
        private LinearLayout editLayout;


        public SimpleViewHolder1(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            imvPhoto = (ImageView) view.findViewById(R.id.imv_photo);
            btnDelete = (ImageView) view.findViewById(R.id.btn_delete);
        }

        public void onBindeViewHolder(OnItemSelectedListener listener) {

            btnDelete.setVisibility(View.GONE);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onAddPhoto();
                }
            });

        }

    }


    public PhotosAdapter(Context context) {
        mContext = context;
    }

    public PhotosAdapter(Context context, ArrayList<Uri> messageModels, OnItemSelectedListener listener) {
        mContext = context;
        this.mItems = messageModels;
        this.listener = listener;
        notifyDataSetChanged();
    }
    public PhotosAdapter(Context context, ArrayList<Uri> messageModels) {
        mContext = context;
        this.mItems = messageModels;
        notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_photos, parent, false);
            return new SimpleViewHolder1(view);
        }else{
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_photos, parent, false);
            return new SimpleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == 1) {
            SimpleViewHolder1 holder = (SimpleViewHolder1) viewHolder;
            holder.onBindeViewHolder(listener);
        }else{
            Uri model = mItems.get(position);
            SimpleViewHolder holder = (SimpleViewHolder) viewHolder;
            holder.onBindeViewHolder(model, listener);
        }
    }

    public void addItem(int position, Uri model) {
        mItems.add(0, model);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position );
        notifyItemRangeChanged(position, mItems.size() );
    }

    public void updateItem(int position, Uri model) {
        mItems.set(position, model);
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.size() == position) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mItems.size() + 1;
    }
}
