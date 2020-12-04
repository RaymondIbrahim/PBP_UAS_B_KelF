package com.tubesb.tubespbp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tubesb.tubespbp.adapter.RecyclerViewAdapter;
import com.tubesb.tubespbp.databinding.ActivityAboutBinding;
import com.tubesb.tubespbp.model.DaftarMahasiswa;
import com.tubesb.tubespbp.model.Mahasiswa;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {
    private ArrayList<Mahasiswa> ListMahasiswa;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        binding.recyclerViewMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMahasiswa.setHasFixedSize(true);

        ListMahasiswa = new DaftarMahasiswa().MAHASISWA;

        recyclerView = findViewById(R.id.recycler_view_mahasiswa);
        adapter = new RecyclerViewAdapter(AboutActivity.this, ListMahasiswa);
        recyclerView.setAdapter(adapter);

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
            //Pass Email dari Login
            String email = getIntent().getStringExtra("email");
            onBackPressed();
            /*Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.putExtra("email", email);*/
        }

        return true;
    }

}
