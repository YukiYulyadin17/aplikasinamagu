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

public class CreateActivityHama extends AppCompatActivity {

    Database database;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    Button btn_simpan, btnChooseHama;
    EditText namahama, detailhama, bentukhama, siklushidup;
    ImageView imageViewHama;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hama);

        database = new Database(this);
        namahama = findViewById(R.id.namahama);
        detailhama = findViewById(R.id.detailhama);
        btn_simpan = findViewById(R.id.btn_simpan);
        btnChooseHama = findViewById(R.id.btnChooseHama);
        imageViewHama = findViewById(R.id.imageViewHama);
        bentukhama = findViewById(R.id.bentukhama);
        siklushidup = findViewById(R.id.siklushidup);
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

                Log.d("CreateActivityHama", "Saving data...");
                Log.d("CreateActivityHama", "Nama Hama: " + namahama.getText().toString());
                Log.d("CreateActivityHama", "Detail Hama: " + detailhama.getText().toString());
                Log.d("CreateActivityHama", "Image Path: " + imagePath);
                Log.d("CreateActivityHama", "Bentuk Hama: " + bentukhama.getText().toString());
                Log.d("CreateActivityHama", "Siklus Hidup: " + siklushidup.getText().toString());
                // Menyimpan data hama dan path gambar ke dalam database
                ContentValues values = new ContentValues();
                values.put(Database.COLUMN_NAMA_HAMA, namahama.getText().toString());
                values.put(Database.COLUMN_DETAIL_HAMA, detailhama.getText().toString());
                values.put(Database.COLUMN_IMAGE_PATH_HAMA, imagePath);
                values.put(Database.COLUMN_BENTUK_HAMA, bentukhama.getText().toString());
                values.put(Database.COLUMN_SIKLUS_HIDUP, siklushidup.getText().toString());

                db.insert(Database.TABLE_HAMA, null, values);

                Toast.makeText(CreateActivityHama.this, "Data tersimpan", Toast.LENGTH_SHORT).show();
                MainActivity.ma.RefreshList();
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
                Log.d("CreateActivityHama", "Image Path: " + imagePath);

                if (imagePath == null || imagePath.isEmpty()) {
                    Log.e("CreateActivityHama", "Image Path is null or empty");
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
