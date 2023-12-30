package com.skripsi.aplikasinamagu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.Nullable;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreateActivityPengHama extends AppCompatActivity {

    Database database;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    Button btn_simpan, btnChooseHama;
    EditText namapenghama, detailpenghama, cakerpenghama;
    ImageView imageViewHama;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_peng_hama);

        database = new Database(this);
        namapenghama = findViewById(R.id.namapenghama);
        detailpenghama = findViewById(R.id.detailpenghama);
        btn_simpan = findViewById(R.id.btn_simpan);
        btnChooseHama = findViewById(R.id.btnChooseHama);
        imageViewHama = findViewById(R.id.imageViewHama);
        cakerpenghama = findViewById(R.id.cakerpenghama);
        btnChooseHama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = database.getWritableDatabase();

                String imagePath = getRealPathFromURI(imageUri);

                Log.d("CreateActivityPengHama", "Saving data...");
                Log.d("CreateActivityPengHama", "Nama Pengendalian Hama: " + namapenghama.getText().toString());
                Log.d("CreateActivityPengHama", "Detail Pengendalian Hama: " + detailpenghama.getText().toString());
                Log.d("CreateActivityPengHama", "Image Path: " + imagePath);
                Log.d("CreateActivityPengHama", "Cara Kerja Pengendalian Hama: " + cakerpenghama.getText().toString());

                ContentValues values = new ContentValues();
                values.put(Database.COLUMN_NAMA_PENG_HAMA, namapenghama.getText().toString());
                values.put(Database.COLUMN_DETAIL_PENG_HAMA, detailpenghama.getText().toString());
                values.put(Database.COLUMN_IMAGE_PATH_PENG_HAMA, imagePath);
                values.put(Database.COLUMN_CAKER_PENG_HAMA, cakerpenghama.getText().toString());

                db.insert(Database.TABLE_PENG_HAMA, null, values);

                Toast.makeText(CreateActivityPengHama.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
                daftarfumigasi.ma.RefreshList();
                finish();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

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
                Log.d("CreateActivityPengHama", "Image Path: " + imagePath);

                // TODO: Pastikan bahwa imagePath tidak null atau kosong
                if (imagePath == null || imagePath.isEmpty()) {
                    Log.e("CreateActivityPengHama", "Image Path is null or empty");
                }
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