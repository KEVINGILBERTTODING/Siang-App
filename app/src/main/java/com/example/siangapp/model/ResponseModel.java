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

    public ResponseModel(Integer code, Boolean status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
