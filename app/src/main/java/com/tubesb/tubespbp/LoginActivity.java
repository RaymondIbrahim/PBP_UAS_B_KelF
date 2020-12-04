package com.tubesb.tubespbp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tubesb.tubespbp.BackEndMobil.ViewsMobil;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private String email, password;
    private Button btnRegister, btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Fragment fragment;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        register = (TextView) findViewById(R.id.registerHere);
        btnLogin = findViewById(R.id.btnLogin);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.tubesb.tubespbp.LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Email is required!");
                    etEmail.requestFocus();
                } else if(!etEmail.getText().toString().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")){
                    etEmail.setError("Please provide valid email!");
                    etEmail.requestFocus();
                } else if(etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Password is required!");
                    etPassword.requestFocus();
                } else if(etPassword.length() <6) {
                    etPassword.setError("Password must be more than 6 characters");
                    etPassword.requestFocus();
                } else if(etEmail.getText().toString().equalsIgnoreCase("admin@gmail.com") && etPassword.getText().toString().equalsIgnoreCase("admin123")){
                    Toast.makeText(LoginActivity.this, "Selamat datang admin!", Toast.LENGTH_LONG).show();
                    loadFragment(new ViewsMobil());
                }else {
                    email = etEmail.getText().toString().trim();
                    password = etPassword.getText().toString().trim();
                    login(email, password);
                }
            }
        });
    }

    private void login(String email, String password) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Login in Progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && !firebaseAuth.getCurrentUser().isEmailVerified()) {
                    fragment = new com.tubesb.tubespbp.VerifyFragment();
                    loadFragment(fragment);
                } else if(task.isSuccessful() && firebaseAuth.getCurrentUser().isEmailVerified()) {
                    Toast.makeText(com.tubesb.tubespbp.LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(com.tubesb.tubespbp.LoginActivity.this, "Your account is not registered yet", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.login_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}