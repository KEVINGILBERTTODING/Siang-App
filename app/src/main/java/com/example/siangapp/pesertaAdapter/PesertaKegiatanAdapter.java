package com.example.siangapp.pesertaAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.R;
import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
import com.example.siangapp.util.PesertaInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesertaKegiatanAdapter extends RecyclerView.Adapter<PesertaKegiatanAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<KegiatanModel> kegiatanModelList;
    PendaftarInterface pendaftarInterface;
    SharedPreferences sharedPreferences;
    PesertaInterface pesertaInterface;

    public PesertaKegiatanAdapter(@NonNull Context context, List<KegiatanModel> kegiatanModelList) {
        this.context = context;
        this.kegiatanModelList = kegiatanModelList;
    }

    public PesertaKegiatanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_kegiatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesertaKegiatanAdapter.ViewHolder holder, int position) {
        holder.tvTanggal.setText(kegiatanModelList.get(holder.getAdapterPosition()).getTanggal());
        holder.tvKegiatan.setText(kegiatanModelList.get(holder.getAdapterPosition()).getIsiKegiatan());
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);



    }

    @Override
    public int getItemCount() {
        return kegiatanModelList.size();
    }

    public void filter (ArrayList<KegiatanModel> filteredList) {
        kegiatanModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTanggal,tvKegiatan;
        CardView cvKegiatan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKegiatan = itemView.findViewById(R.id.tvKegiatan);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            cvKegiatan = itemView.findViewById(R.id.cvKegiatan);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("user_id", null);
            pesertaInterface = DataApi.getClient().create(PesertaInterface.class);


            AlertDialog.Builder dialogprogress = new AlertDialog.Builder(context);
            dialogprogress.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
            AlertDialog progressBar = dialogprogress.create();
            progressBar.show();


            pendaftarInterface.getUserById(userId).enqueue(new Callback<PendaftarModel>() {
                @Override
                public void onResponse(Call<PendaftarModel> call, Response<PendaftarModel> response) {
                    if (response.isSuccessful() && response.body() != null) {


                        // menapilkan formulir pendaftaran jika user belum
                        // menaftar
                        if (response.body().getAcc().equals("lolos")) {
                            Dialog dialogKegiatan = new Dialog(context);
                            dialogKegiatan.setContentView(R.layout.layout_kegiatan);
                            dialogKegiatan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            Button btnSimpan, btnBatal, btnDelete;
                            EditText etKegiatan;
                            btnSimpan = dialogKegiatan.findViewById(R.id.btnSimpan);
                            btnBatal = dialogKegiatan.findViewById(R.id.btnBatal);
                            btnDelete = dialogKegiatan.findViewById(R.id.btnDelete);

                            etKegiatan = dialogKegiatan.findViewById(R.id.etKegiatan);
                            etKegiatan.setText(kegiatanModelList.get(getAdapterPosition()).getIsiKegiatan());
                            dialogKegiatan.show();

                            btnDelete.setVisibility(View.VISIBLE);
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder dialogprogress = new AlertDialog.Builder(context);
                                    dialogprogress.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
                                    AlertDialog progressBar = dialogprogress.create();
                                    progressBar.show();

                                    pesertaInterface.deleteKegiatan(String.valueOf(kegiatanModelList.get(getAdapterPosition()).getId()))
                                            .enqueue(new Callback<ResponseModel>() {
                                                @Override
                                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                                        progressBar.dismiss();
                                                        dialogKegiatan.dismiss();
                                                        Toasty.success(context, "Berhasil menghapus kegiatan", Toasty.LENGTH_SHORT).show();
                                                        kegiatanModelList.remove(getAdapterPosition());
                                                        notifyDataSetChanged();
                                                    } else {
                                                        progressBar.dismiss();
                                                        Toasty.error(context, "Terjadi kesalahan", Toasty.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                    progressBar.dismiss();
                                                    Toasty.error(context, "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();

                                                }
                                            });

                                }
                            });


                            btnBatal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogKegiatan.dismiss();
                                }
                            });

                            btnSimpan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (etKegiatan.getText().toString().isEmpty()) {
                                        etKegiatan.setError("Field kegiatan tidak boleh kosong");
                                        etKegiatan.requestFocus();
                                        return;
                                    } else {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                        alert.setTitle("Loading...").setMessage("Menyimpan data...");
                                        AlertDialog progressBar = alert.create();
                                        progressBar.show();

                                        HashMap map = new HashMap();
                                        map.put("isi_kegiatan", RequestBody.create(MediaType.parse("text/plain"), etKegiatan.getText().toString()));
                                        map.put("id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kegiatanModelList.get(getAdapterPosition()).getId())));


                                        pesertaInterface.updateKegiatan(map).enqueue(new Callback<ResponseModel>() {
                                            @Override
                                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                if (response.isSuccessful() && response.body().getCode() == 200) {
                                                    progressBar.dismiss();
                                                    dialogKegiatan.dismiss();
                                                    kegiatanModelList.get(getAdapterPosition()).setIsiKegiatan(etKegiatan.getText().toString());
                                                    notifyDataSetChanged();
                                                    Toasty.success(context, "Berhasil mengubah kegiatan baru", Toasty.LENGTH_SHORT).show();


                                                }else {
                                                    progressBar.dismiss();
                                                    Toasty.error(context, "Gagal mengubah kegiatan baru", Toasty.LENGTH_SHORT).show();

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                progressBar.dismiss();
                                                Toasty.error(context, "Tidak ada koneksi interner", Toasty.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }
                            });

                            dialogKegiatan.show();

                        }
                        progressBar.dismiss();

                    }else {
                        progressBar.dismiss();
                        Toasty.error(context, "Gagal memuat data...", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PendaftarModel> call, Throwable t) {
                    progressBar.dismiss();
                    Toasty.error(context, "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();

                }
            });

        }
    }
}
