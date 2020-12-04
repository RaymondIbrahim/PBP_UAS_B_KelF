package com.tubesb.tubespbp.api;

import com.tubesb.tubespbp.dao.UserDAO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class UserResponse {
    @SerializedName("data")
    @Expose
    private List<UserDAO> users = null;

    @SerializedName("message")
    @Expose
    private String message;

    public List<UserDAO> getUsers() {
        return users;
    }

    public String getMessage() {
        return message;
    }

    public void setUsers(List<UserDAO> users) {
        this.users = users;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
