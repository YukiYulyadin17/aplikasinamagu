package com.skripsi.aplikasinamagu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<HamaModel> hamaList;
    private ViewPager2 viewPager2;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(HamaModel hama);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    SlideAdapter(List<HamaModel> hamaList, ViewPager2 viewPager2) {
        this.hamaList = hamaList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Log.d("SlideAdapter", "onBindViewHolder: " + position);
        holder.setImage(hamaList.get(position));

        holder.itemView.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {

                onItemClickListener.onItemClick(hamaList.get(adapterPosition));
            }
        });

        if (position == hamaList.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return hamaList.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;
        private TextView hamaNameTextView;

        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            hamaNameTextView = itemView.findViewById(R.id.hamaNameTextView);
        }

        void setImage(HamaModel hama) {
            Glide.with(itemView.getContext())
                    .load(hama.getimagepathhama())
                    .into(imageView);

            hamaNameTextView.setText(hama.getnamahama());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hamaList.addAll(hamaList);
            notifyDataSetChanged();
        }
    };

}

