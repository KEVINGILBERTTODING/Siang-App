package com.example.siangapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DivisiModel implements Serializable {
    @SerializedName("id")
    Integer id;
    @SerializedName("divisi")
    String divisi;

    public DivisiModel(Integer id, String divisi) {
        this.id = id;
        this.divisi = divisi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }
}
