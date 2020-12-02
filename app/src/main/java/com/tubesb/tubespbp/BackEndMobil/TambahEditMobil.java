package com.tubesb.tubespbp.BackEndMobil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.tubesb.tubespbp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.POST;

public class TambahEditMobil extends Fragment {

    private TextInputEditText txtNama, txtTransmisi, txtHarga, txtSeat;
    private ImageView ivGambar;
    private Button btnSimpan, btnBatal, btnUnggah;
    private String status, selected, encoder;
    private int id;
    private Mobil mobil;
    private View view;
    private Bitmap bitmap;
    private Uri selectedImage = null;
    private static final int PERMISSION_CODE = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tambah_edit_mobil, container, false);
        init();
        setAttribut();

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
        if(menu.findItem(R.id.btnSearch) != null)
            menu.findItem(R.id.btnSearch).setVisible(false);
        if(menu.findItem(R.id.btnAdd) != null)
            menu.findItem(R.id.btnAdd).setVisible(false);
    }

    public void init(){
        mobil   = (Mobil) getArguments().getSerializable("mobil");
        txtNama             = view.findViewById(R.id.txtNama);
        txtTransmisi        = view.findViewById(R.id.txtTransmisi);
        txtHarga            = view.findViewById(R.id.txtHarga);
        txtSeat             = view.findViewById(R.id.txtSeat);
        btnSimpan           = view.findViewById(R.id.btnSimpan);
        btnBatal            = view.findViewById(R.id.btnBatal);
        btnUnggah           = view.findViewById(R.id.btnUnggah);
        ivGambar            = view.findViewById(R.id.ivGambar);

        status = getArguments().getString("status");
        if(status.equals("edit"))
        {
            id = mobil.getId();
            txtNama.setText(mobil.getNama_mobil());
            txtTransmisi.setText(mobil.getJenis_transmisi());
            txtHarga.setText(mobil.getHarga());
            txtSeat.setText(mobil.getJumlah_seat());
            /*
            Glide.with(view.getContext())
                    .load(MobilAPI.URL_IMAGE +mobil.getImageURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivGambar);*/
        }
    }

    private void setAttribut() {
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                            PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openCamera();
                            }
                        }
                        else{
                            openCamera();
                        }
                        alertD.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="galeri";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openGallery();
                            }
                        }
                        else{
                            openGallery();
                        }
                        alertD.dismiss();
                    }
                });

                alertD.setView(view);
                alertD.show();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = txtNama.getText().toString();
                String transmisi = txtTransmisi.getText().toString();
                String harga = txtHarga.getText().toString();
                String seat = txtSeat.getText().toString();


                if(nama.isEmpty() || transmisi.isEmpty() || harga.isEmpty() || seat.isEmpty())
                    Toast.makeText(getContext(), "Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                else{
                    mobil = new Mobil(nama, transmisi, harga, seat);
                    if(status.equals("tambah"))
                        tambahMobil(nama, transmisi, harga, seat);
                    else
                        editMobil(nama, transmisi, harga, seat);
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ViewsMobil());
            }
        });
    }

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(selected.equals("kamera"))
                        openCamera();
                    else
                        openGallery();
                }else{
                    Toast.makeText(getContext() ,"Permision denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            selectedImage = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,bao);
            byte [] ba = bao.toByteArray();
            encoder = Base64.encodeToString(ba, Base64.DEFAULT);
        }
        else if(resultCode == RESULT_OK && requestCode == 2)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte [] ba = bao.toByteArray();
            encoder = Base64.encodeToString(ba, Base64.DEFAULT);
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false);
        }
        fragmentTransaction.replace(R.id.frame_tambah_edit_mobil, fragment)
                .detach(this)
                .attach(this)
                .commit();
    }

    public void closeFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(TambahEditMobil.this).detach(this)
                .attach(this).commit();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void tambahMobil(final String nama, final String transmisi, final String harga, final String seat){
        //Tambahkan tambah mobil disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data mobil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, MobilAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    Toast.makeText(getContext(), "Tambah Mobil Berhasil", Toast.LENGTH_SHORT).show();

                    loadFragment(new ViewsMobil());

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
                params.put("nama_mobil", nama);
                params.put("jenis_transmisi", transmisi);
                params.put("harga", harga);
                params.put("jumlah_seat", seat);
                if(encoder!=null)
                    params.put("imageURL", encoder);
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void editMobil(final String nama, final String transmisi, final String harga, final String seat) {
        //Tambahkan edit mobil disini
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data mobil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, MobilAPI.URL_UPDATE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    loadFragment(new ViewsMobil());
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
                params.put("nama_mobil", nama);
                params.put("jenis_transmisi", transmisi);
                params.put("harga", harga);
                params.put("jumlah_seat", seat);
                if(encoder!=null)
                    params.put("gambar", encoder);
                return params;

            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
