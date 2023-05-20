package com.example.siangapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KegiatanModel implements Serializable {
    @SerializedName("id")
    Integer id;
    @SerializedName("isi_kegiatan")
    String isiKegiatan;
    @SerializedName("tanggal")
    String tanggal;
    @SerializedName("user_id")
    String userId;

    public KegiatanModel(Integer id, String isiKegiatan, String tanggal, String userId) {
        this.id = id;
        this.isiKegiatan = isiKegiatan;
        this.tanggal = tanggal;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsiKegiatan() {
        return isiKegiatan;
    }

    public void setIsiKegiatan(String isiKegiatan) {
        this.isiKegiatan = isiKegiatan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
