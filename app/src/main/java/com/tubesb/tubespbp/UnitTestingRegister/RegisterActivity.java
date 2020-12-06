package com.tubesb.tubespbp.UnitTestingRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.tubesb.tubespbp.R;
import com.tubesb.tubespbp.dao.UserDAO;

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterActivity extends AppCompatActivity implements RegisterView{

    private Button btnRegister;
    private TextInputEditText email, password, nama, alamat, noTelp;
    private RegisterPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnSubmit);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        nama = findViewById(R.id.etNama);
        alamat = findViewById(R.id.etAlamat);
        noTelp = findViewById(R.id.etTelp);
        presenter = new RegisterPresenter(this, new RegisterService());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onRegisterClicked();
            }
        });
    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }
    @Override
    public void showEmailError(String message) {
        email.setError(message);
    }
    @Override
    public String getPassword() {
        return password.getText().toString();
    }
    @Override
    public void showPasswordError(String message) {
        password.setError(message);
    }
    @Override
    public String getNama() {
        return nama.getText().toString();
    }
    @Override
    public void showNamaError(String message) {
        nama.setError(message);
    }
    @Override
    public String getAlamat() {
        return alamat.getText().toString();
    }
    @Override
    public void showAlamatError(String message) {
        alamat.setError(message);
    }
    @Override
    public String getnoTelp() {
        return noTelp.getText().toString();
    }
    @Override
    public void shownoTelpError(String message) {
        noTelp.setError(message);
    }
    @Override
    public void startMainActivity() {
        new ActivityUtil(this).startMainActivity();
    }
    @Override
    public void startUserProfileActivity(UserDAO user) {
        new ActivityUtil(this).startUserProfile(user);
    }
    @Override
    public void showRegisterError(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }
    @Override
    public void showErrorResponse(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }
}