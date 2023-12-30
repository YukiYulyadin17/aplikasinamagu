package com.skripsi.aplikasinamagu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Integer> _id;
    private ArrayList<String> namapenghama;
    private ArrayList<String> detailpenghama;
    private ArrayList<String> imagepathpenghama;


    public MyAdapter(Context context, ArrayList<Integer> _id, ArrayList<String> namapenghama, ArrayList<String> detailpenghama, ArrayList<String> imagepathpenghama) {
        this.context = context;
        this._id = _id;
        this.namapenghama = namapenghama;
        this.detailpenghama = detailpenghama;
        this.imagepathpenghama = imagepathpenghama;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.namapenghama.setText(namapenghama.get(position));
        holder.detailpenghama.setText(detailpenghama.get(position));

        Log.d("ImagePath", "Path: " + imagepathpenghama.get(position));

        Glide.with(context).load(imagepathpenghama.get(position)).into(holder.imagepathpenghama);
    }

    @Override
    public int getItemCount() {
        return namapenghama.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namapenghama, detailpenghama;
        ImageView imagepathpenghama;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namapenghama = itemView.findViewById(R.id.titleTextView);
            detailpenghama = itemView.findViewById(R.id.detailTextView);
            imagepathpenghama = itemView.findViewById(R.id.imageView);
        }
    }

    public void updateData(ArrayList<String> namapenghamaList, ArrayList<String> detailpenghamaList, ArrayList<String> imagepathpenghamaList) {
        this.namapenghama = namapenghamaList;
        this.detailpenghama = detailpenghamaList;
        this.imagepathpenghama = imagepathpenghamaList;
        notifyDataSetChanged();
    }

}
