package com.tubesb.tubespbp.FrontEnd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tubesb.tubespbp.LoginActivity;
import com.tubesb.tubespbp.R;
import com.tubesb.tubespbp.api.ApiClient;
import com.tubesb.tubespbp.api.ApiInterface;
import com.tubesb.tubespbp.api.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProfileActivity extends AppCompatActivity {

    private TextView twNama, twAlamat, twEmail, twTelp;
    private String email, sNama, sAlamat, sEmail, sTelp, id;
    private ProgressDialog progressDialog;
    private Button btnLogout, btnEdit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        twNama = findViewById(R.id.twNama);
        twAlamat = findViewById(R.id.twAlamat);
        twEmail = findViewById(R.id.twEmail);
        twTelp = findViewById(R.id.twTelp);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(com.tubesb.tubespbp.FrontEnd.ShowProfileActivity.this, EditUserActivity.class);
                i.putExtra("email", email);
                i.putExtra("id", id);
                startActivityForResult(i, 0);
            }
        });


        email = getIntent().getStringExtra("email");
        loadUserById(email);
        Toast.makeText(ShowProfileActivity.this, email, Toast.LENGTH_SHORT).show();

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    private void loadUserById(String email) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserByEmail(email, "data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                id = response.body().getUsers().get(0).getId();
                sNama = response.body().getUsers().get(0).getNama();
                sAlamat = response.body().getUsers().get(0).getAlamat();
                sEmail = response.body().getUsers().get(0).getEmail();
                sTelp = response.body().getUsers().get(0).getNoTelp();

                twNama.setText(sNama);
                twAlamat.setText(sAlamat);
                twEmail.setText(sEmail);
                twTelp.setText(sTelp);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(com.tubesb.tubespbp.FrontEnd.ShowProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_dashboard){
            onBackPressed();
        }

        return true;
    }

}