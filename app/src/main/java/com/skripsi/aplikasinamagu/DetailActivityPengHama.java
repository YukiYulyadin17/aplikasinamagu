package com.skripsi.aplikasinamagu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class DetailActivityPengHama extends AppCompatActivity {

    protected Cursor cursor;
    Database database;
    TextView namapenghama, detailpenghama, cakerpenghama;
    ImageView imageViewHama;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peng_hama);

        database = new Database(this);
        namapenghama = findViewById(R.id.namapenghama);
        detailpenghama = findViewById(R.id.detailpenghama);
        imageViewHama = findViewById(R.id.imageViewHama);
        cakerpenghama = findViewById(R.id.cakerpenghama);

        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM peng_hama WHERE namapenghama = ?",
                new String[]{getIntent().getStringExtra("namapenghama")});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int namaIndex = cursor.getColumnIndex(Database.COLUMN_NAMA_PENG_HAMA);
                int detailIndex = cursor.getColumnIndex(Database.COLUMN_DETAIL_PENG_HAMA);
                int imagePathIndex = cursor.getColumnIndex(Database.COLUMN_IMAGE_PATH_PENG_HAMA);
                int bentukIndex = cursor.getColumnIndex(Database.COLUMN_CAKER_PENG_HAMA);


                if (namaIndex != -1) {
                    namapenghama.setText(cursor.getString(namaIndex));
                }

                if (detailIndex != -1) {
                    detailpenghama.setText(cursor.getString(detailIndex));
                }
                if (bentukIndex != -1) {
                    cakerpenghama.setText(cursor.getString(bentukIndex));
                }

                int namapenghama = cursor.getColumnIndex(Database.COLUMN_NAMA_PENG_HAMA);
                int detailpenghama = cursor.getColumnIndex(Database.COLUMN_DETAIL_PENG_HAMA);
                int cakerpenghama = cursor.getColumnIndex(Database.COLUMN_CAKER_PENG_HAMA);


                Log.d("DetailActivityPengHama", "Nama Pengendalian Hama: " + cursor.getString(namapenghama));
                Log.d("DetailActivityPengHama", "Detail Pengendalian Hama: " + cursor.getString(detailpenghama));
                Log.d("DetailActivityPengHama", "Cara Kerja: " + cursor.getString(cakerpenghama));

                if (imagePathIndex != -1) {
                    String imagePath = cursor.getString(imagePathIndex);
                    Log.d("DetailActivityPengHama", "Image path: " + imagePath); // Pernyataan Log
                    if (imagePath != null && !imagePath.isEmpty()) {

                        Picasso.get().load("file://" + imagePath).into(imageViewHama);
                    } else {

                    }
                }
            }

            cursor.close();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);

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