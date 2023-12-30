package com.skripsi.aplikasinamagu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hama.db";
    private static final int DATABASE_VERSION = 8; // Updated version number

    public static final String TABLE_HAMA = "hama";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAMA_HAMA = "namahama";
    public static final String COLUMN_DETAIL_HAMA = "detailhama";
    public static final String COLUMN_IMAGE_PATH_HAMA = "imagepathhama";
    public static final String COLUMN_BENTUK_HAMA = "bentukhama";
    public static final String COLUMN_SIKLUS_HIDUP = "siklushidup";

    public static final String TABLE_PENG_HAMA = "peng_hama";
    public static final String COLUMN_ID_PENG_HAMA = "_id";
    public static final String COLUMN_NAMA_PENG_HAMA = "namapenghama";
    public static final String COLUMN_DETAIL_PENG_HAMA = "detailpenghama";
    public static final String COLUMN_IMAGE_PATH_PENG_HAMA = "imagepathpenghama";
    public static final String COLUMN_CAKER_PENG_HAMA = "cakerpenghama";

    public static final String TABLE_PROFIL = "tbl_profil";
    public static final String COLUMN_ID_PROFIL = "_idprofil";
    public static final String COLUMN_NAMA_PROFIL = "namaprof";
    public static final String COLUMN_ALAMAT = "alamat";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PENDIDIKAN = "pendidikan";
    public static final String COLUMN_UNIT_KERJA = "unitkerja";
    public static final String COLUMN_JABATAN = "jabatan";
    public static final String COLUMN_IMAGE_PROFIL = "imageprofil";

    public static final String TABLE_PROFIL_2 = "tbl_profil2";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlHama = "CREATE TABLE " + TABLE_HAMA + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAMA_HAMA + " TEXT, "
                + COLUMN_DETAIL_HAMA + " TEXT, "
                + COLUMN_IMAGE_PATH_HAMA + " TEXT, "
                + COLUMN_BENTUK_HAMA + " TEXT, "
                + COLUMN_SIKLUS_HIDUP + " TEXT);";
        db.execSQL(sqlHama);

        String sqlPengHama = "CREATE TABLE " + TABLE_PENG_HAMA + " ("
                + COLUMN_ID_PENG_HAMA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAMA_PENG_HAMA + " TEXT, "
                + COLUMN_DETAIL_PENG_HAMA + " TEXT, "
                + COLUMN_IMAGE_PATH_PENG_HAMA + " TEXT, "
                + COLUMN_CAKER_PENG_HAMA + " TEXT);";
        db.execSQL(sqlPengHama);

        String sqlProfil = "CREATE TABLE " + TABLE_PROFIL + " ("
                + COLUMN_ID_PROFIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAMA_PROFIL + " TEXT, "
                + COLUMN_ALAMAT + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PENDIDIKAN + " TEXT, "
                + COLUMN_UNIT_KERJA + " TEXT, "
                + COLUMN_JABATAN + " TEXT, "
                + COLUMN_IMAGE_PROFIL + " TEXT);";
        db.execSQL(sqlProfil);

        // Tambahkan tabel baru tbl_profil2 dengan struktur yang sama
        String sqlProfil2 = "CREATE TABLE " + TABLE_PROFIL_2 + " ("
                + COLUMN_ID_PROFIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAMA_PROFIL + " TEXT, "
                + COLUMN_ALAMAT + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PENDIDIKAN + " TEXT, "
                + COLUMN_UNIT_KERJA + " TEXT, "
                + COLUMN_JABATAN + " TEXT, "
                + COLUMN_IMAGE_PROFIL + " TEXT);";
        db.execSQL(sqlProfil2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Database", "onUpgrade: oldVersion=" + oldVersion + ", newVersion=" + newVersion);

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_HAMA + " ADD COLUMN " + COLUMN_IMAGE_PATH_HAMA + " TEXT;");
        }

        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_HAMA + " ADD COLUMN " + COLUMN_BENTUK_HAMA + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_HAMA + " ADD COLUMN " + COLUMN_SIKLUS_HIDUP + " TEXT;");
        }

        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_PENG_HAMA + " ADD COLUMN " + COLUMN_IMAGE_PATH_PENG_HAMA + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_PENG_HAMA + " ADD COLUMN " + COLUMN_CAKER_PENG_HAMA + " TEXT;");
        }

        if (oldVersion < 5) {
            String sqlProfil = "CREATE TABLE " + TABLE_PROFIL + " ("
                    + COLUMN_ID_PROFIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAMA_PROFIL + " TEXT, "
                    + COLUMN_ALAMAT + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_PENDIDIKAN + " TEXT, "
                    + COLUMN_UNIT_KERJA + " TEXT, "
                    + COLUMN_JABATAN + " TEXT, "
                    + COLUMN_IMAGE_PROFIL + " TEXT);";
            db.execSQL(sqlProfil);
        }

        if (oldVersion >= 6) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFIL);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public Cursor getAllHama() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_HAMA, null);
    }

    public Cursor getAllProfil() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PROFIL, null);
    }

    public Cursor getAllProfil2() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PROFIL_2, null);
    }

    public Cursor getAllPenghama() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PENG_HAMA, null);
    }

    public long insertProfil(String nama, String alamat, String email, String pendidikan, String unitkerja, String jabatan, String imagepath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_PROFIL, nama);
        values.put(COLUMN_ALAMAT, alamat);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PENDIDIKAN, pendidikan);
        values.put(COLUMN_UNIT_KERJA, unitkerja);
        values.put(COLUMN_JABATAN, jabatan);
        values.put(COLUMN_IMAGE_PROFIL, imagepath);
        return db.insert(TABLE_PROFIL, null, values);
    }

    public long insertProfil2(String nama, String alamat, String email, String pendidikan, String unitkerja, String jabatan, String imagepath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_PROFIL, nama);
        values.put(COLUMN_ALAMAT, alamat);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PENDIDIKAN, pendidikan);
        values.put(COLUMN_UNIT_KERJA, unitkerja);
        values.put(COLUMN_JABATAN, jabatan);
        values.put(COLUMN_IMAGE_PROFIL, imagepath);
        return db.insert(TABLE_PROFIL_2, null, values);
    }
}
