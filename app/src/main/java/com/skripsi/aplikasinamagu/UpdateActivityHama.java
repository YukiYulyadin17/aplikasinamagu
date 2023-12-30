package com.skripsi.aplikasinamagu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class UpdateActivityHama extends AppCompatActivity {
    protected Cursor cursor;
    Database database;
    Button btn_simpan, btnChooseImage;
    EditText namahama, detailhama, bentukhama, siklushidup;
    ImageView imageViewHama;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hama);

        database = new Database(this);
        namahama = findViewById(R.id.namahama);
        detailhama = findViewById(R.id.detailhama);
        btn_simpan = findViewById(R.id.btn_simpan);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imageViewHama = findViewById(R.id.imageViewHama);
        bentukhama = findViewById(R.id.bentukhama);
        siklushidup = findViewById(R.id.siklushidup);

        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM hama WHERE namahama = '" +
                getIntent().getStringExtra("namahama") + "'", null);

        if (cursor != null && cursor.moveToFirst()) {

            int namaIndex = cursor.getColumnIndex("namahama");
            int detailIndex = cursor.getColumnIndex("detailhama");
            int imagePathIndex = cursor.getColumnIndex("imagepathhama");
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
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file://" + imagePath));
                        imageViewHama.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            cursor.close();
        }

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (imageUri != null) {
                        SQLiteDatabase db = database.getWritableDatabase();
                        String imagePath = getRealPathFromURI(imageUri);

                        db.execSQL("UPDATE hama SET namahama = '" +
                                namahama.getText().toString() + "', detailhama = '" +
                                detailhama.getText().toString() + "', bentukhama = '" +
                                bentukhama.getText().toString() + "', siklushidup = '" +
                                siklushidup.getText().toString() + "', imagepathhama = '" +
                                imagePath + "' WHERE namahama = '" +
                                getIntent().getStringExtra("namahama") + "'");

                        Toast.makeText(UpdateActivityHama.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                        MainActivity.ma.RefreshList();
                        finish();
                    } else {
                        Toast.makeText(UpdateActivityHama.this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(UpdateActivityHama.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewHama.setImageBitmap(bitmap);

                String imagePath = getRealPathFromURI(imageUri);
                Log.d("CreateActivityHama", "Image Path: " + imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            File tempFile = File.createTempFile("temp", null, getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
