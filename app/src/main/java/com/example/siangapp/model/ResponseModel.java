package com.example.siangapp.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    @SerializedName("code")
    Integer code;
    @SerializedName("status")
    Boolean status;
    @SerializedName("message")
    String message;
    @SerializedName("nama_peserta")
    String namaPeserta;
    @SerializedName("id")
    String userId;
    @SerializedName("role_id")
    Integer roleId;
    @SerializedName("divisi")
    String divisi;

    public ResponseModel(Integer code, String divisi, Boolean status, String message, String namaPeserta, String userId, Integer roleId) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.namaPeserta = namaPeserta;
        this.userId = userId;
        this.roleId = roleId;
        this.divisi = divisi;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNamaPeserta() {
        return namaPeserta;
    }

    public void setNamaPeserta(String namaPeserta) {
        this.namaPeserta = namaPeserta;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }
}
