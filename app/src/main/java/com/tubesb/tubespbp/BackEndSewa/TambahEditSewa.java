package com.tubesb.tubespbp.BackEndSewa;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tubesb.tubespbp.BackEndMobil.Mobil;
import com.tubesb.tubespbp.BackEndMobil.MobilAPI;
import com.tubesb.tubespbp.R;
import com.tubesb.tubespbp.api.ApiClient;
import com.tubesb.tubespbp.api.ApiInterface;
import com.tubesb.tubespbp.api.UserResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class TambahEditSewa extends Fragment {
    private TextInputEditText txtNama, txtAlamat, txtTanggal, txtLama;
    private Button btnSimpan, btnBatal;
    private String status, selected, idUser, email;
    private String sNama, sAlamat, sEmail, sTelp;
    private int id;
    private Sewa sewa;
    private View view;
    private static final int PERMISSION_CODE = 1000;
    AutoCompleteTextView autoCompleteTextView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction, transaction;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userID;
    ArrayList<String> pilihan = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tambah_edit_sewa, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        getMobil();
        init();
        setAttribut();

        autoCompleteTextView = view.findViewById(R.id.txtMobil);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown, pilihan);
        autoCompleteTextView.setAdapter(arrayAdapter);

        //Ambil data email dari ViewsSewa
        email = getArguments().getString("email");
        FindUserId(email);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(menu.findItem(R.id.btnBack) != null)
            menu.findItem(R.id.btnBack).setVisible(false);
        if(menu.findItem(R.id.btnSearch) != null)
            menu.findItem(R.id.btnSearch).setVisible(false);
        if(menu.findItem(R.id.btnAdd) != null)
            menu.findItem(R.id.btnAdd).setVisible(false);
    }

    public void init(){
        sewa   = (Sewa) getArguments().getSerializable("sewa");
        txtNama                 = view.findViewById(R.id.txtNama);
        txtAlamat               = view.findViewById(R.id.txtAlamat);
        txtTanggal              = view.findViewById(R.id.txtTanggal);
        autoCompleteTextView    = view.findViewById(R.id.txtMobil);
        txtLama                 = view.findViewById(R.id.txtLama);
        btnSimpan               = view.findViewById(R.id.btnSimpan);
        btnBatal                = view.findViewById(R.id.btnBatal);

        FindUserId(email);

        status = getArguments().getString("status");
        if(status.equals("edit"))
        {
            id = sewa.getId();
            txtNama.setText(sewa.getNama());
            txtAlamat.setText(sewa.getAlamat());
            txtTanggal.setText(sewa.getTgl_sewa());
            autoCompleteTextView.setText(sewa.getPilihan_mobil());
            txtLama.setText(sewa.getLama_sewa());
        }
    }

    private void setAttribut() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = txtNama.getText().toString();
                String id_penyewa = firebaseUser.getUid();
                String alamat = txtAlamat.getText().toString();
                String tgl_sewa = txtTanggal.getText().toString();
                String mobil = autoCompleteTextView.getText().toString();
                String lama = txtLama.getText().toString();

                if(nama.isEmpty() || alamat.isEmpty() || tgl_sewa.isEmpty() || mobil.isEmpty() || lama.isEmpty())
                    Toast.makeText(getContext(), "Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                else{
                    sewa = new Sewa(nama, id_penyewa, alamat, mobil, tgl_sewa, lama);
                    if(status.equals("tambah"))
                        tambahSewa(nama, id_penyewa, alamat, tgl_sewa, mobil, lama);
                    else
                        editSewa(nama, id_penyewa, alamat, tgl_sewa, mobil, lama);
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("email", email);
                ViewsSewa viewsSewa = new ViewsSewa();
                viewsSewa.setArguments(data);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager .beginTransaction()
                        .replace(R.id.frame_view_sewa, viewsSewa)
                        .commit();
            }
        });
    }


    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false);
        }
        fragmentTransaction.replace(R.id.frame_tambah_edit_sewa, fragment)
                .detach(this)
                .attach(this)
                .commit();
    }

    public void closeFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(TambahEditSewa.this).detach(this)
                .attach(this).commit();
    }


    public void tambahSewa(final String nama, final String id_penyewa, final String alamat, final String tgl_sewa, final String mobil, final String lama){
        //Tambahkan tambah mobil disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data sewa");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, SewaAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(getContext(), "Tambah Sewa Berhasil", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    ViewsSewa viewsSewa = new ViewsSewa();
                    viewsSewa.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager .beginTransaction()
                            .replace(R.id.frame_view_sewa, viewsSewa)
                            .commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("id_penyewa", id_penyewa);
                params.put("alamat", alamat);
                params.put("pilihan_mobil", mobil);
                params.put("tgl_sewa", tgl_sewa);
                params.put("lama_sewa", lama);
                return params;
            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void editSewa(final String nama, final String id_penyewa, final String alamat, final String tgl_sewa, final String mobil, final String lama){
        //Tambahkan edit sewa disini
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data mobil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, SewaAPI.URL_UPDATE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    Toast.makeText(getContext(), "Edit Sewa Berhasil", Toast.LENGTH_SHORT).show();;

                    Bundle data = new Bundle();
                    data.putString("email", email);
                    ViewsSewa viewsSewa = new ViewsSewa();
                    viewsSewa.setArguments(data);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager .beginTransaction()
                            .replace(R.id.frame_view_sewa, viewsSewa)
                            .commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("id_penyewa", id_penyewa);
                params.put("alamat", alamat);
                params.put("pilihan_mobil", mobil);
                params.put("tgl_sewa", tgl_sewa);
                params.put("lama_sewa", lama);
                return params;

            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }


    private void FindUserId(String email) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserByEmail(email, "data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                sNama = response.body().getUsers().get(0).getNama();
                sAlamat = response.body().getUsers().get(0).getAlamat();

                txtNama.setText(sNama);
                txtAlamat.setText(sAlamat);
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    public void getMobil() {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MobilAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("MOBILREQ", "json onResponse: " + jsonObject.toString());

                        pilihan.add(jsonObject.optString("nama_mobil"));
                    }

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
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}