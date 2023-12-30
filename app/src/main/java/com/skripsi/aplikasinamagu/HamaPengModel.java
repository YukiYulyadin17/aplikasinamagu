package com.skripsi.aplikasinamagu;

public class HamaPengModel {
    private int _id;
    private String namapenghama;
    private String detailpenghama;
    private String imagepathpenghama;

    public HamaPengModel(int id, String namapenghama, String detailpenghama, String imagepathpenghama) {
        this._id = id;
        this.namapenghama = namapenghama;
        this.detailpenghama = detailpenghama;
        this.imagepathpenghama = imagepathpenghama;
    }

    public int getId() {
        return _id;
    }

    public String getnamapenghama() {
        return namapenghama;
    }

    public String getdetailpenghama() {
        return detailpenghama;
    }

    public String getimagepathpenghama() {
        return imagepathpenghama;
    }
}
