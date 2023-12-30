package com.skripsi.aplikasinamagu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class DetailActivityHama extends AppCompatActivity {

    protected Cursor cursor;
    Database database;
    TextView namahama, detailhama, bentukhama, siklushidup;
    ImageView imageViewHama;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hama);

        database = new Database(this);
        namahama = findViewById(R.id.namahama);
        detailhama = findViewById(R.id.detailhama);
        imageViewHama = findViewById(R.id.imageViewHama);
        bentukhama = findViewById(R.id.bentukhama);
        siklushidup = findViewById(R.id.siklushidup);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("namahama")) {
            String namahamaFromIntent = intent.getStringExtra("namahama");

            SQLiteDatabase db = database.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM hama WHERE namahama = ?", new String[]{namahamaFromIntent});

            if (cursor != null && cursor.moveToFirst()) {
                int namaIndex = cursor.getColumnIndex(Database.COLUMN_NAMA_HAMA);
                int detailIndex = cursor.getColumnIndex(Database.COLUMN_DETAIL_HAMA);
                int imagePathIndex = cursor.getColumnIndex(Database.COLUMN_IMAGE_PATH_HAMA);
                int bentukIndex = cursor.getColumnIndex(Database.COLUMN_BENTUK_HAMA);
                int siklusIndex = cursor.getColumnIndex(Database.COLUMN_SIKLUS_HIDUP);

                if (namaIndex != -1) {
                    namahama.setText(cursor.getString(namaIndex));
                }

                if (detailIndex != -1) {
                    detailhama.setText(cursor.getString(detailIndex));
                }

                if (bentukIndex != -1) {
                    bentukhama.setText(cursor.getString(bentukIndex));
                }

                if (siklusIndex != -1) {
                    siklushidup.setText(cursor.getString(siklusIndex));
                }

                if (imagePathIndex != -1) {
                    String imagePath = cursor.getString(imagePathIndex);

                    if (imagePath != null && !imagePath.isEmpty()) {

                        Picasso.get().load("file://" + imagePath).into(imageViewHama);
                    }
                }

                cursor.close();
            }
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.house) {
                Intent intentHome = new Intent(this, home.class);
                startActivity(intentHome);
            } else if (item.getItemId() == R.id.shorts) {
                Intent intentHalProfil = new Intent(this, halprofil.class);
                startActivity(intentHalProfil);
            } else if (item.getItemId() == R.id.subscriptions) {
                Intent intentHalAbout = new Intent(this, halabout.class);
                startActivity(intentHalAbout);
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
