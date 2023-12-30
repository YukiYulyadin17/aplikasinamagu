package com.skripsi.aplikasinamagu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class halprofil extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    Database database;
    TextView namaprofTextView, alamatTextView, pendidikanTextView, emailTextView, unitkerjaTextView, jabatanTextView;
    ImageView ImageViewProfil, ImageViewProfil2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halprofil);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        namaprofTextView = findViewById(R.id.namaprof);
        alamatTextView = findViewById(R.id.alamat);
        pendidikanTextView = findViewById(R.id.pendidikan);
        emailTextView = findViewById(R.id.email);
        unitkerjaTextView = findViewById(R.id.unitkerja);
        jabatanTextView = findViewById(R.id.jabatan);
        ImageViewProfil = findViewById(R.id.imageViewIconap);
        ImageViewProfil2 = findViewById(R.id.icedit);

        ImageViewProfil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(halprofil.this, updateprofil.class);
                startActivity(intent);
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

        displayProfileData();
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

    private void displayProfileData() {
        database = new Database(this);
        Cursor cursor = database.getAllProfil2();

        if (cursor.moveToFirst()) {
            String nama = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAMA_PROFIL));
            String alamat = cursor.getString(cursor.getColumnIndex(Database.COLUMN_ALAMAT));
            String pendidikan = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PENDIDIKAN));
            String email = cursor.getString(cursor.getColumnIndex(Database.COLUMN_EMAIL));
            String unitkerja = cursor.getString(cursor.getColumnIndex(Database.COLUMN_UNIT_KERJA));
            String jabatan = cursor.getString(cursor.getColumnIndex(Database.COLUMN_JABATAN));
            String imagePath = cursor.getString(cursor.getColumnIndex(Database.COLUMN_IMAGE_PROFIL));

            namaprofTextView.setText(nama);
            alamatTextView.setText(alamat);
            pendidikanTextView.setText(pendidikan);
            emailTextView.setText(email);
            unitkerjaTextView.setText(unitkerja);
            jabatanTextView.setText(jabatan);

            // Load image using Glide
            if (imagePath != null && !imagePath.isEmpty()) {
                Glide.with(this)
                        .load(imagePath)
                        .into(ImageViewProfil);
            }
        }

        cursor.close();
        database.close();
    }

}
