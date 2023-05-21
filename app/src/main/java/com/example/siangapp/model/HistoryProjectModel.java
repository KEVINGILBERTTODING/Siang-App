package com.example.siangapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HistoryProjectModel implements Serializable {
    @SerializedName("id")
    String id;
    @SerializedName("user_id")
    String userId;
    @SerializedName("isi_kegiatan")
    String isiKegiatan;
    @SerializedName("note")
    String note;
    @SerializedName("date_created")
    String dateCreated;

    public HistoryProjectModel(String id, String userId, String isiKegiatab, String note, String dateCreated) {
        this.id = id;
        this.userId = userId;
        this.isiKegiatan = isiKegiatab;
        this.note = note;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsiKegiatab() {
        return isiKegiatan;
    }

    public void setIsiKegiatab(String isiKegiatab) {
        this.isiKegiatan = isiKegiatab;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
