package com.tubesb.tubespbp.FrontEnd;

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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tubesb.tubespbp.BackEndMobil.AdapterMobil;
import com.tubesb.tubespbp.BackEndMobil.Mobil;
import com.tubesb.tubespbp.BackEndMobil.MobilAPI;
import com.tubesb.tubespbp.DashboardActivity;
import com.tubesb.tubespbp.LoginActivity;
import com.tubesb.tubespbp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class LihatMobil extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMobil adapter;
    private List<Mobil> listMobil;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_mobil, container, false);

        Bundle bundle = this.getArguments();
        String email = bundle.getString("email");
        Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
        loadDaftarMobil();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_dashboard).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_dashboard) {
            Bundle bundle = this.getArguments();
            String email = bundle.getString("email");
            Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void loadDaftarMobil(){
        setAdapter();
        getMobil();
    }

    public void setAdapter(){
        getActivity().setTitle("Data Mobil");
        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/
        listMobil = new ArrayList<Mobil>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterMobil(view.getContext(), listMobil, new AdapterMobil.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarMobil();
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

    public void getMobil() {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data mobil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MobilAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listMobil.isEmpty())
                        listMobil.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("MOBILREQ", "json onResponse: " + jsonObject.toString());

                        int id                  = Integer.parseInt(jsonObject.optString("id"));
                        String nama             = jsonObject.optString("nama_mobil");
                        String transmisi        = jsonObject.optString("jenis_transmisi");
                        String harga            = jsonObject.optString("harga");
                        String gambar           = jsonObject.optString("imageURL");
                        String seat             = jsonObject.optString("jumlah_seat");

                        Mobil mobil = new Mobil(id, nama, transmisi, harga, gambar, seat);
                        listMobil.add(mobil);
                    }
                    adapter.notifyDataSetChanged();

                    Log.i("MOBILREQ", "try onResponse: " + response.optString("message"));
                }catch (JSONException e){
                    Log.i("MOBILREQ", "catch onResponse: " + e.getMessage());
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
