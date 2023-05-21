package com.example.siangapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProjectModel implements Serializable {
@SerializedName("user_id")
      String userId;
@SerializedName("judul_tugas")
      String judulTugas;
@SerializedName("deskripsi_tugas")
      String deskripsiTugas;
@SerializedName("tanggal_mulai")
      String tglMulai;
@SerializedName("tanggal_selesai")
      String tglSelesai;
@SerializedName("progress")
      String progress;

      public ProjectModel(String userId, String judulTugas, String deskripsiTugas, String tglMulai, String tglSelesai, String progress) {
            this.userId = userId;
            this.judulTugas = judulTugas;
            this.deskripsiTugas = deskripsiTugas;
            this.tglMulai = tglMulai;
            this.tglSelesai = tglSelesai;
            this.progress = progress;
      }

      public String getUserId() {
            return userId;
      }

      public void setUserId(String userId) {
            this.userId = userId;
      }

      public String getJudulTugas() {
            return judulTugas;
      }

      public void setJudulTugas(String judulTugas) {
            this.judulTugas = judulTugas;
      }

      public String getDeskripsiTugas() {
            return deskripsiTugas;
      }

      public void setDeskripsiTugas(String deskripsiTugas) {
            this.deskripsiTugas = deskripsiTugas;
      }

      public String getTglMulai() {
            return tglMulai;
      }

      public void setTglMulai(String tglMulai) {
            this.tglMulai = tglMulai;
      }

      public String getTglSelesai() {
            return tglSelesai;
      }

      public void setTglSelesai(String tglSelesai) {
            this.tglSelesai = tglSelesai;
      }

      public String getProgress() {
            return progress;
      }

      public void setProgress(String progress) {
            this.progress = progress;
      }
}
