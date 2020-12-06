package com.tubesb.tubespbp.BackEndSewa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tubesb.tubespbp.DashboardActivity;
import com.tubesb.tubespbp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ViewsSewa extends Fragment {

    private RecyclerView recyclerView;
    private AdapterSewa adapter;
    private List<Sewa> listSewa;
    private View view;
    public String email,id_Penyewa;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_sewa, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        id_Penyewa = firebaseUser.getUid();

        email = getArguments().getString("email");

        loadDaftarSewa();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bar_mobil, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.btnSearch);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.btnBack).setVisible(true);
        menu.findItem(R.id.btnSearch).setVisible(true);
        menu.findItem(R.id.btnAdd).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnAdd) {
            Bundle data = new Bundle();
            data.putString("status", "tambah");
            data.putString("email", email);
            TambahEditSewa tambahEditSewa = new TambahEditSewa();
            tambahEditSewa.setArguments(data);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager .beginTransaction()
                    .replace(R.id.frame_view_sewa, tambahEditSewa)
                    .commit();
        }
        else if (id == R.id.btnBack) {
            Intent intent = new Intent(getContext(), DashboardActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDaftarSewa(){
        setAdapter();
        getSewa();
    }

    public void setAdapter(){
        getActivity().setTitle("Data Persewaan");

        Bundle data = new Bundle();
        data.putString("email", email);

        listSewa = new ArrayList<Sewa>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterSewa(view.getContext(), listSewa, email,  new AdapterSewa.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarSewa();
                }
            }
        });


        GridLayoutManager layoutManager = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? new GridLayoutManager(getContext(), 1)
                : new GridLayoutManager(getContext(), 4);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getSewa() {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data sewa");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, SewaAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listSewa.isEmpty())
                        listSewa.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("SEWAREQ", "json onResponse: " + jsonObject.toString());

                        String idPenyewa          = jsonObject.optString("id_penyewa");
                        if (id_Penyewa.equals(idPenyewa)) {
                            int id = Integer.parseInt(jsonObject.optString("id"));
                            String nama = jsonObject.optString("nama");
                            String alamat = jsonObject.optString("alamat");
                            String mobil = jsonObject.optString("pilihan_mobil");
                            String tgl_sewa = jsonObject.optString("tgl_sewa");
                            String lama_sewa = jsonObject.optString("lama_sewa");

                            Sewa sewa = new Sewa(id, nama, id_Penyewa, alamat, mobil, tgl_sewa, lama_sewa);
                            listSewa.add(sewa);
                        }
                    }
                    adapter.notifyDataSetChanged();

                    Log.i("SEWAREQ", "try onResponse: " + response.optString("message"));
                }catch (JSONException e){
                    Log.i("SEWAREQ", "catch onResponse: " + e.getMessage());
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

}
