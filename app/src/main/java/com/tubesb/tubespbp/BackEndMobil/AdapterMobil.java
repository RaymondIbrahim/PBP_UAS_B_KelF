package com.tubesb.tubespbp.BackEndMobil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.tubesb.tubespbp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.POST;

public class AdapterMobil extends RecyclerView.Adapter<AdapterMobil.adapterMobilViewHolder>{
    private List<Mobil> mobilList;
    private List<Mobil> mobilListFiltered;
    private Context context;
    private View view;
    private deleteItemListener mListener;

    public AdapterMobil(Context context, List<Mobil> mobilList,
                       deleteItemListener mListener) {
        this.context            = context;
        this.mobilList           = mobilList;
        this.mobilListFiltered   = mobilList;
        this.mListener          = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public adapterMobilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_mobil, parent, false);
        return new adapterMobilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterMobilViewHolder holder, int position) {
        final Mobil mobil = mobilListFiltered.get(position);

        holder.twNama.setText(mobil.getNama_mobil());
        holder.twTransmisi.setText(mobil.getJenis_transmisi());
        holder.twHarga.setText(mobil.getHarga());
        holder.twSeat.setText(mobil.getJumlah_seat());
        Glide.with(holder.ivImg.getContext())
                .load(mobil.getImageURL())
                .into(holder.ivImg);


        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle data = new Bundle();
                data.putSerializable("mobil", mobil);
                data.putString("status", "edit");
                TambahEditMobil tambahEditMobil = new TambahEditMobil();
                tambahEditMobil.setArguments(data);
                loadFragment(tambahEditMobil);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus mobil ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMobil(mobil.getId());
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
        return (mobilListFiltered != null) ? mobilListFiltered.size() : 0;
    }

    public class adapterMobilViewHolder extends RecyclerView.ViewHolder {
        private TextView twNama, twTransmisi, twHarga, ivEdit, ivHapus, twSeat;
        private ImageButton ivImg;
        private CardView cardBuku;

        public adapterMobilViewHolder(@NonNull View itemView) {
            super(itemView);
            twNama = itemView.findViewById(R.id.tvNama);
            twTransmisi = itemView.findViewById(R.id.tvTransmisi);
            twHarga = itemView.findViewById(R.id.tvHarga);
            twSeat = itemView.findViewById(R.id.tvSeat);
            ivImg = itemView.findViewById(R.id.ivImg);
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
                    mobilListFiltered = mobilList;
                }
                else {
                    List<Mobil> filteredList = new ArrayList<>();
                    for(Mobil mobil : mobilList) {
                        if(String.valueOf(mobil.getNama_mobil()).toLowerCase().contains(userInput) ||
                                mobil.getJenis_transmisi().toLowerCase().contains(userInput)) {
                            filteredList.add(mobil);
                        }
                    }
                    mobilListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mobilListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mobilListFiltered = (ArrayList<Mobil>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_mobil,fragment)
                .commit();
    }

    public void deleteMobil(int id){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data mobil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, MobilAPI.URL_DELETE + id, new Response.Listener<String>() {
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
                    loadFragment(new ViewsMobil());
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
