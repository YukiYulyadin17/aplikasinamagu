package com.skripsi.aplikasinamagu;

import android.content.ContentValues;
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

public class updateprofil extends AppCompatActivity {
    protected Cursor cursor;
    Database database;
    Button btn_simpan, btnChooseImage;
    EditText namaprof, alamat, email, pendidikan, unitkerja, jabatan;
    ImageView imageprofil;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofil);

        database = new Database(this);
        namaprof = findViewById(R.id.namaprof);
        alamat = findViewById(R.id.alamat);
        btn_simpan = findViewById(R.id.btn_simpan);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imageprofil = findViewById(R.id.imageprofil);
        email = findViewById(R.id.email);
        pendidikan = findViewById(R.id.pendidikan);
        unitkerja = findViewById(R.id.unitkerja);
        jabatan = findViewById(R.id.jabatan);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TABLE_PROFIL_2, null);

        if (cursor.moveToFirst()) {

            String nama = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAMA_PROFIL));
            String alamatStr = cursor.getString(cursor.getColumnIndex(Database.COLUMN_ALAMAT));
            String emailStr = cursor.getString(cursor.getColumnIndex(Database.COLUMN_EMAIL));
            String pendidikanStr = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PENDIDIKAN));
            String unitkerjaStr = cursor.getString(cursor.getColumnIndex(Database.COLUMN_UNIT_KERJA));
            String jabatanStr = cursor.getString(cursor.getColumnIndex(Database.COLUMN_JABATAN));
            String imagePath = cursor.getString(cursor.getColumnIndex(Database.COLUMN_IMAGE_PROFIL));

            namaprof.setText(nama);
            alamat.setText(alamatStr);
            email.setText(emailStr);
            pendidikan.setText(pendidikanStr);
            unitkerja.setText(unitkerjaStr);
            jabatan.setText(jabatanStr);

            if (imagePath != null && !imagePath.isEmpty()) {
                Uri imageUri = Uri.fromFile(new File(imagePath));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imageprofil.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();
        db.close();

        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
                imageprofil.setImageBitmap(bitmap);

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
    private void updateData() {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        String nama = namaprof.getText().toString();
        String alamatStr = alamat.getText().toString();
        String emailStr = email.getText().toString();
        String pendidikanStr = pendidikan.getText().toString();
        String unitkerjaStr = unitkerja.getText().toString();
        String jabatanStr = jabatan.getText().toString();

        values.put(Database.COLUMN_NAMA_PROFIL, nama);
        values.put(Database.COLUMN_ALAMAT, alamatStr);
        values.put(Database.COLUMN_EMAIL, emailStr);
        values.put(Database.COLUMN_PENDIDIKAN, pendidikanStr);
        values.put(Database.COLUMN_UNIT_KERJA, unitkerjaStr);
        values.put(Database.COLUMN_JABATAN, jabatanStr);

        if (imageUri != null) {
            String imagePath = getRealPathFromURI(imageUri);
            values.put(Database.COLUMN_IMAGE_PROFIL, imagePath);
        }

        db.update(Database.TABLE_PROFIL_2, values, null, null);

        db.close();

        Intent intent = new Intent(updateprofil.this, halprofil.class);
        startActivity(intent);
        finish();
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
