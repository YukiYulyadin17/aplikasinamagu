package com.skripsi.aplikasinamagu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> namapenghama, detailpenghama, imagepathpenghama;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    private MyAdapter adapter;
    private Database database;

    private void fetchDataFromDatabase() {
        Cursor cursor2 = database.getAllPenghama();

        namapenghama.clear();
        detailpenghama.clear();
        imagepathpenghama.clear();

        if (cursor2 != null) {
            try {
                if (cursor2.moveToFirst()) {
                    do {

                        String namahama = cursor2.getString(cursor2.getColumnIndex("namahama"));
                        String detailhama = cursor2.getString(cursor2.getColumnIndex("detailhama"));
                        String imagepathhama = cursor2.getString(cursor2.getColumnIndex("imagepathhama"));

                        namapenghama.add(namahama);
                        detailpenghama.add(detailhama);
                        imagepathpenghama.add(imagepathhama);

                    } while (cursor2.moveToNext());
                } else {

                    Toast.makeText(home.this, "Tidak ada data penghama", Toast.LENGTH_SHORT).show();
                }
            } finally {

                cursor2.close();
            }
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager2 = findViewById(R.id.viewPager);
        CardView cardViewHama = findViewById(R.id.tombolhama);
        CardView cardViewHama2 = findViewById(R.id.tombolfumigasi);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        database = new Database(this);
        namapenghama = new ArrayList<>();
        detailpenghama = new ArrayList<>();
        imagepathpenghama = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("EEEE", new Locale("id", "ID"));
        SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        String day = dateFormatDay.format(calendar.getTime());
        String date = dateFormatDate.format(calendar.getTime());

        TextView dayTextView = findViewById(R.id.dayTextView);
        dayTextView.setText(day);

        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(date);

        Log.d("DateLog", "Date value: " + date);
        Toast.makeText(home.this, "Date value: " + date, Toast.LENGTH_SHORT).show();
        adapter = new MyAdapter(this, new ArrayList<>(), namapenghama, detailpenghama, imagepathpenghama);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();

        List<HamaModel> hamaList = new ArrayList<>();
        Cursor cursor = database.getAllHama();
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String namahama = cursor.getString(cursor.getColumnIndex("namahama"));
                String detailhama = cursor.getString(cursor.getColumnIndex("detailhama"));
                String imagepathhama = cursor.getString(cursor.getColumnIndex("imagepathhama"));
                hamaList.add(new HamaModel(_id, namahama, detailhama, imagepathhama));
            } while (cursor.moveToNext());
        }
        cursor.close();


        cardViewHama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cardViewHama2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, daftarfumigasi.class);
                startActivity(intent);
            }
        });
        SlideAdapter slideAdapter = new SlideAdapter(hamaList, viewPager2);
        slideAdapter.setOnItemClickListener(new SlideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HamaModel hama) {
                Log.d("ItemClick", "Item clicked: " + hama.getnamahama());

                Intent intent = new Intent(home.this, DetailActivityHama.class);
                intent.putExtra("hamaModel", hama);
                startActivity(intent);
            }
        });


        viewPager2.setAdapter(slideAdapter);
        viewPager2.setAdapter(new SlideAdapter(hamaList, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositionTransform = new CompositePageTransformer();
        compositionTransform.addTransformer(new MarginPageTransformer(40));
        compositionTransform.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositionTransform);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable, 2000);
            }
        });

        setSupportActionBar(toolbar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.house) {
                Intent intent = new Intent(this, home.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.shorts) {
                Intent intent = new Intent(this, halprofil.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.subscriptions) {
                Intent intent = new Intent(this, halabout.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.library) {
                showExitConfirmationDialog();
            }
            return true;
        });
    }

    private void displaydata() {
        Cursor cursor2 = database.getAllPenghama();

        namapenghama.clear();
        detailpenghama.clear();
        imagepathpenghama.clear();

        ArrayList<String> namapenghamaList = new ArrayList<>();
        ArrayList<String> detailpenghamaList = new ArrayList<>();
        ArrayList<String> imagepathpenghamaList = new ArrayList<>();

        if (cursor2 != null && cursor2.moveToFirst()) {
            int namapenghamaColumnIndex = cursor2.getColumnIndex("namapenghama");
            int detailpenghamaaColumnIndex = cursor2.getColumnIndex("detailpenghama");
            int imagepathpenghamaColumnIndex = cursor2.getColumnIndex("imagepathpenghama");

            do {
                String namapenghama = cursor2.getString(namapenghamaColumnIndex);
                String detailpenghama = cursor2.getString(detailpenghamaaColumnIndex);
                String imagepathpenghama = cursor2.getString(imagepathpenghamaColumnIndex);

                namapenghamaList.add(namapenghama);
                detailpenghamaList.add(detailpenghama);
                imagepathpenghamaList.add(imagepathpenghama);
            } while (cursor2.moveToNext());

            cursor2.close();
        } else {
            Toast.makeText(home.this, "Tidak ada data penghama", Toast.LENGTH_SHORT).show();
        }

        adapter.updateData(namapenghamaList, detailpenghamaList, imagepathpenghamaList);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 3000);
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yakin ingin keluar aplikasi?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
