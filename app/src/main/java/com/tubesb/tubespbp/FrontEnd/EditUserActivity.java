package com.tubesb.tubespbp.FrontEnd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.GsonBuilder;
import com.tubesb.tubespbp.R;
import com.tubesb.tubespbp.RegisterActivity;
import com.tubesb.tubespbp.api.ApiClient;
import com.tubesb.tubespbp.api.ApiInterface;
import com.tubesb.tubespbp.api.UserResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private EditText etNama, etAlamat, etTelp;
    private MaterialButton btnCancel, btnUpdate;
    private ProgressDialog progressDialog;
    private String email, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        Log.i("EDITUSERID", "ID User: " + email);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etTelp = findViewById(R.id.etTelp);

        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNama.getText().toString().isEmpty()) {
                    etNama.setError("Isikan dengan benar");
                    etNama.requestFocus();
                }
                else if(etAlamat.getText().toString().isEmpty()) {
                    etAlamat.setError("Isikan dengan benar");
                    etAlamat.requestFocus();
                }
                else if(etTelp.getText().toString().isEmpty()) {
                    etTelp.setError("Isikan dengan benar");
                    etTelp.requestFocus();
                }
                else {
                    progressDialog.show();
                    Toast.makeText(EditUserActivity.this, id + "IDNya", Toast.LENGTH_SHORT).show();
                    update();
                }
            }
        });

        loadUser(email);
    }

    public void loadUser(String email) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.getUserByEmail(email, "data");

        Toast.makeText(EditUserActivity.this, id + "IDNya", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                String nama = response.body().getUsers().get(0).getNama();
                String alamat = response.body().getUsers().get(0).getAlamat();
                String Telp = response.body().getUsers().get(0).getNoTelp();

                etNama.setText(nama);
                etAlamat.setText(alamat);
                etTelp.setText(Telp);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(com.tubesb.tubespbp.FrontEnd.EditUserActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                Log.i("EDITERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }


    public void update() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> req = apiService.updateUser(id, etNama.getText().toString(), etAlamat.getText().toString(),
                etTelp.getText().toString());

        req.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(EditUserActivity.this, "Berhasil update user", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, "Gagal menambah user", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}