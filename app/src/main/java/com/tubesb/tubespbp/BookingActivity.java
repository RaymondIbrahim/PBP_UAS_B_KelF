package com.tubesb.tubespbp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tubesb.tubespbp.adapter.PenyewaRecyclerViewAdapter;
import com.tubesb.tubespbp.database.DatabaseClient;
import com.tubesb.tubespbp.model.Penyewa;

import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private SearchView searchInput;
    private FloatingActionButton addBtn;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private PenyewaRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        recyclerView();
        addPenyewa();
        refreshLayout();
        getPenyewa();
        search();
    }

    private void recyclerView() {
        recyclerView = findViewById(R.id.user_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addPenyewa() {
        addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPenyewaFragment add = new AddPenyewaFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, add)
                        .commit();

            }
        });
    }

    private void refreshLayout() {
        refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPenyewa();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getPenyewa(){
        class GetPenyewa extends AsyncTask<Void, Void, List<Penyewa>> {

            @Override
            protected List<Penyewa> doInBackground(Void... voids) {
                List<Penyewa> penyewaList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getDatabase()
                        .penyewaDao()
                        .getAll();
                return penyewaList;
            }

            @Override
            protected void onPostExecute(List<Penyewa> penyewas) {
                super.onPostExecute(penyewas);
                adapter = new PenyewaRecyclerViewAdapter(BookingActivity.this, penyewas);
                recyclerView.setAdapter(adapter);
                if (penyewas.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                }
            }
        }

        GetPenyewa get = new GetPenyewa();
        get.execute();
    }

    private void search() {
        searchInput = findViewById(R.id.input_search);
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
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
