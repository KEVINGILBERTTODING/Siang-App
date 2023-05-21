package com.example.siangapp.model;

import com.example.siangapp.util.DataApi;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendaftarModel implements Serializable {
    @SerializedName("id")
    String userId;

    @SerializedName("nama_peserta")
    String name;

    @SerializedName("nim")
    String nim;

    @SerializedName("image")
    String image;



    @SerializedName("alamat")
    String address;

    @SerializedName("jenis_kel")
    String gender;

    @SerializedName("agama")
    String religion;

    @SerializedName("asal_sekolah")
    String school;

    @SerializedName("alamat_sekolah")
    String schoolAddress;

    @SerializedName("jurusan")
    String major;

    @SerializedName("email")
    String email;

    @SerializedName("no_telp")
    String phoneNumber;

    @SerializedName("password")
    String password;

    @SerializedName("role_id")
    Integer roleId;

    @SerializedName("data_created")
    String dataCreated;

    @SerializedName("divisi")
    Integer division;

    @SerializedName("surat_ket")
    String certificate;

    @SerializedName("tgl_mulai")
    String startDate;

    @SerializedName("tgl_selesai")
    String endDate;

    @SerializedName("acc")
    String acc;

    @SerializedName("alasan")
    String reason;

    @SerializedName("status")
    Integer status;

    @SerializedName("penilaian")
    Integer rating;

    @SerializedName("tugas")
    Integer task;

    @SerializedName("inovasi")
    String inovasi;

    @SerializedName("kerja_sama")
    String kerjaSama;
    @SerializedName("disiplin")
    String disiplin;
    @SerializedName("rata")
    String rata;


    public PendaftarModel(String userId, String disiplin, String kerjaSama, String inovasi
                          ,String rata, String name, String nim, String image, String address, String gender, String religion, String school, String schoolAddress, String major, String email, String phoneNumber, String password, Integer roleId, String dataCreated, Integer division, String certificate, String startDate, String endDate, String acc, String reason, Integer status, Integer rating, Integer task) {
        this.userId = userId;
        this.name = name;
        this.nim = nim;
        this.image = image;
        this.address = address;
        this.gender = gender;
        this.religion = religion;
        this.school = school;
        this.schoolAddress = schoolAddress;
        this.major = major;
        this.email = email;
        this.inovasi = inovasi;
        this.disiplin = disiplin;
        this.kerjaSama = kerjaSama;
        this.rata = rata;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roleId = roleId;
        this.dataCreated = dataCreated;
        this.division = division;
        this.certificate = certificate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.acc = acc;
        this.reason = reason;
        this.status = status;
        this.rating = rating;
        this.task = task;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getImage() {
        return DataApi.URL_PHOTO_PROFILE + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(String dataCreated) {
        this.dataCreated = dataCreated;
    }

    public Integer getDivision() {
        return division;
    }

    public void setDivision(Integer division) {
        this.division = division;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public String getInovasi() {
        return inovasi;
    }

    public void setInovasi(String inovasi) {
        this.inovasi = inovasi;
    }

    public String getKerjaSama() {
        return kerjaSama;
    }

    public void setKerjaSama(String kerjaSama) {
        this.kerjaSama = kerjaSama;
    }

    public String getDisiplin() {
        return disiplin;
    }

    public void setDisiplin(String disiplin) {
        this.disiplin = disiplin;
    }

    public String getRata() {
        return rata;
    }

    public void setRata(String rata) {
        this.rata = rata;
    }
}
