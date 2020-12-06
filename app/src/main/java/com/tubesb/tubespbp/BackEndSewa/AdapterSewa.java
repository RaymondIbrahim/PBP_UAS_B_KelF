package com.tubesb.tubespbp.BackEndSewa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tubesb.tubespbp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.POST;

public class AdapterSewa  extends RecyclerView.Adapter<AdapterSewa.adapterSewaViewHolder>{
    private List<Sewa> sewaList;
    private List<Sewa> sewaListFiltered;
    private Context context;
    private View view;
    private deleteItemListener mListener;
    private String email;

    public AdapterSewa(Context context, List<Sewa> sewaList,
                       String email, deleteItemListener mListener) {
        this.context            = context;
        this.sewaList           = sewaList;
        this.email              = email;
        this.sewaListFiltered   = sewaList;
        this.mListener          = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public adapterSewaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_sewa, parent, false);

        return new adapterSewaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterSewaViewHolder holder, int position) {
        final Sewa sewa = sewaListFiltered.get(position);

        holder.twNama.setText(sewa.getNama());
        holder.twAlamat.setText(sewa.getAlamat());
        holder.twMobil.setText(sewa.getPilihan_mobil());
        holder.twTgl.setText(sewa.getTgl_sewa());
        holder.twLama.setText(sewa.getLama_sewa());


        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle data = new Bundle();
                data.putSerializable("sewa", sewa);
                data.putString("status", "edit");
                data.putString("email", email);
                TambahEditSewa tambahEditSewa = new TambahEditSewa();
                tambahEditSewa.setArguments(data);
                loadFragment(tambahEditSewa);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus data sewa ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSewa(sewa.getId());
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (sewaListFiltered != null) ? sewaListFiltered.size() : 0;
    }

    public class adapterSewaViewHolder extends RecyclerView.ViewHolder {
        private TextView twNama, twAlamat, twMobil, twTgl, twLama, ivEdit, ivHapus;
        private CardView cardBuku;

        public adapterSewaViewHolder(@NonNull View itemView) {
            super(itemView);
            twNama = itemView.findViewById(R.id.tvNama);
            twAlamat = itemView.findViewById(R.id.tvAlamat);
            twMobil = itemView.findViewById(R.id.tvMobil);
            twTgl = itemView.findViewById(R.id.tvTanggal);
            twLama = itemView.findViewById(R.id.tvLama);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
            cardBuku        = itemView.findViewById(R.id.cardBuku);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    sewaListFiltered = sewaList;
                }
                else {
                    List<Sewa> filteredList = new ArrayList<>();
                    for(Sewa sewa : sewaList) {
                        if(String.valueOf(sewa.getNama()).toLowerCase().contains(userInput) ||
                                sewa.getPilihan_mobil().toLowerCase().contains(userInput)) {
                            filteredList.add(sewa);
                        }
                    }
                    sewaListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sewaListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sewaListFiltered = (ArrayList<Sewa>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_sewa,fragment)
                .commit();
    }

    public void deleteSewa(int id){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data sewa");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, SewaAPI.URL_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasul tidak yerdapat gangguan.error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();

                    Bundle data = new Bundle();
                    data.putString("email", email);
                    ViewsSewa viewsSewa = new ViewsSewa();
                    viewsSewa.setArguments(data);
                    loadFragment(viewsSewa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika respoonse jaringan terdapat gangguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahanb request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}