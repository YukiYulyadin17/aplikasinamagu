package com.skripsi.aplikasinamagu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    String[] daftar;
    ListView listView;
    protected Cursor cursor;
    Database database;
    public static MainActivity ma;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(MainActivity.this, CreateActivityHama.class);
                startActivity(pindah);
            }
        });
        ma = this;
        database = new Database(this);
        listView = findViewById(R.id.lisView);
        RefreshList();
    }
    public void RefreshList() {
        SQLiteDatabase db = null;
        try {
            db = database.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM hama", null);
            daftar = new String[cursor.getCount()];

            if (cursor != null && cursor.moveToFirst()) {
                int i = 0;
                do {
                    int columnIndex = cursor.getColumnIndex(Database.COLUMN_NAMA_HAMA);
                    if (columnIndex != -1) {
                        daftar[i] = cursor.getString(columnIndex);
                        i++;
                    }
                } while (cursor.moveToNext());
            }

            if (listView != null) {
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daftar));
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (daftar != null && daftar.length > arg2) {
                        final String selection = daftar[arg2];
                        showOptionsDialog(selection);
                    }
                }
            });

            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

     private void showOptionsDialog(final String selection) {
        final CharSequence[] dialogItems = {"Lihat hama", "Update hama", "Hapus hama"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pilihan");
        builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent i = new Intent(getApplicationContext(), DetailActivityHama.class);
                        i.putExtra("namahama", selection);
                        startActivity(i);
                        break;
                    case 1:
                        Intent in = new Intent(getApplicationContext(), UpdateActivityHama.class);
                        in.putExtra("namahama", selection);
                        startActivity(in);
                        break;
                    case 2:
                        showDeleteConfirmationDialog(selection);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final String selection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yakin ingin menghapus data " + selection + "?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(selection);
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

    private void deleteData(String selection) {
        SQLiteDatabase dn = null;
        try {
            dn = database.getWritableDatabase();
            dn.execSQL("DELETE FROM hama WHERE namahama = '" + selection + "'");
            RefreshList();
        } finally {
            if (dn != null && dn.isOpen()) {
                dn.close();
            }
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
